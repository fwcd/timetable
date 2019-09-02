package fwcd.timetable.model.query.cau;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import fwcd.fructose.Option;
import fwcd.fructose.exception.Rethrow;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.utils.IdGenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UnivISXmlParser {
	private static final DocumentBuilderFactory PARSER_FACTORY = DocumentBuilderFactory.newInstance();
	private static final DateTimeFormatter DATE_PARSER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter TIME_PARSER = DateTimeFormatter.ofPattern("H:mm");
	private final DocumentBuilder parser;

	public UnivISXmlParser() {
		try {
			parser = PARSER_FACTORY.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new Rethrow(e);
		}
	}
	
	private class PathedOutputNode {
		UnivISQueryOutputNode node;
		List<String> path;
	}
	
	public UnivISQueryResult parse(String raw) throws IOException {
		try {
			Document document = parser.parse(new InputSource(new StringReader(raw)));
			NodeList events = document.getElementsByTagName("Event");
			NodeList lectures = document.getElementsByTagName("Lecture");
			Stream<PathedOutputNode> pathedOutputs = Stream.concat(
				streamNodes(lectures).map(it -> parseXmlLecture(it, document)),
				streamNodes(events).map(it -> parseXmlEvent(it, document))
			);
			
			return new UnivISQueryResult(treeify("Ergebnisse", pathedOutputs));
		} catch (SAXException e) {
			// Ignore: This is an input error
		} catch (NullPointerException e) {
			// Do not ignore: This is a programming error
			e.printStackTrace();
		}
		return new UnivISQueryResult(raw, "UnivIS Query");
	}
	
	private UnivISQueryOutputNode.Parent treeify(String name, Stream<PathedOutputNode> outputs) {
		UnivISQueryOutputNode.Parent root = new UnivISQueryOutputNode.Parent(name);
		UnivISQueryOutputNode.Parent others = new UnivISQueryOutputNode.Parent("Unsortiert");
		
		outputs.forEach(pathed -> {
			UnivISQueryOutputNode.Parent node = root;
			
			for (String segment : pathed.path) {
				node = node.getOrAddChildParent(segment, /* highlighted */ false);
			}
			
			if (node == root) {
				others.addChild(pathed.node);
			} else {
				node.addChild(pathed.node);
			}
		});
		
		if (!others.hasNoChildren()) {
			root.addChildParent(others);
		}
		return root;
	}
	
	private PathedOutputNode parseXmlLecture(Node node, Document document) {
		String name = childValueByPath(node, "name").orElse("???");
		List<UnivISQueryOutputNode.Leaf> terms = childsByPath(node, "terms", "term")
			.map(it -> parseXmlLectureTerm(node, document, it, name))
			.collect(Collectors.toList());
		
		PathedOutputNode result = new PathedOutputNode();
		result.path = new ArrayList<>();
		
		if (terms.size() == 1) {
			result.node = terms.get(0);
		} else {
			result.node = new UnivISQueryOutputNode.PreLeaf(name, terms);
		}
		
		resolveParentPath(node, document, result.path);
		return result;
	}
	
	private PathedOutputNode parseXmlEvent(Node node, Document document) {
		String name = childValueByPath(node, "title").orElse("???");
		List<UnivISQueryOutputNode.Leaf> terms = childsByPath(node, "terms", "term")
			.map(it -> parseXmlEventTerm(node, document, it, name))
			.collect(Collectors.toList());
		
		PathedOutputNode result = new PathedOutputNode();
		result.path = new ArrayList<>();
		
		if (terms.size() == 1) {
			result.node = terms.get(0);
		} else {
			result.node = new UnivISQueryOutputNode.PreLeaf(name, terms);
		}
		
		resolveParentPath(node, document, result.path);
		return result;
	}
	
	private UnivISQueryOutputNode.Leaf parseXmlEventTerm(Node node, Document document, Node termNode, String name) {
		Option<LocalDate> startDate = childValueByPath(node, "startdate").map(this::parseDate);
		Option<LocalDate> endDate = childValueByPath(node, "enddate").map(this::parseDate);
		Option<LocalTime> startTime = childValueByPath(termNode, "starttime").map(this::parseTime);
		Option<LocalTime> endTime = childValueByPath(termNode, "endtime").map(this::parseTime);
		AppointmentModel.Builder builder = new AppointmentModel.Builder(name, IdGenerator.MISSING_ID);
		
		if (startDate.isPresent() && endDate.isPresent()) {
			builder
				.startDate(startDate.unwrap())
				.lastDate(endDate.unwrap())
				.ignoreDate(false);
		} else {
			builder.ignoreDate(true);
		}
		
		if (startTime.isPresent() && endTime.isPresent()) {
			builder
				.startTime(startTime.unwrap())
				.endTime(endTime.unwrap())
				.ignoreTime(false);
		} else {
			builder.ignoreTime(true);
		}
		
		return new UnivISQueryOutputNode.Leaf(builder.build());
	}
	
	private UnivISQueryOutputNode.Leaf parseXmlLectureTerm(Node node, Document document, Node termNode, String name) {
		Option<LocalDate> startDate = childValueByPath(termNode, "startdate").or(() -> childValueByPath(node, "startdate")).map(this::parseDate);
		Option<LocalDate> endDate = childValueByPath(termNode, "enddate").or(() -> childValueByPath(node, "enddate")).map(this::parseDate);
		Option<LocalTime> startTime = childValueByPath(termNode, "starttime").map(this::parseTime);
		Option<LocalTime> endTime = childValueByPath(termNode, "endtime").map(this::parseTime);
		Option<String> repeat = childValueByPath(termNode, "repeat");
		Option<String> room = childByPath(termNode, "room", "UnivISRef")
			.flatMap(it -> resolveUnivISRef(it, document))
			.flatMap(it -> childValueByPath(it, "short")); // TODO: Use UnivISRef to categorize by rooms?
		Set<LocalDate> excludes = childValueByPath(termNode, "exclude")
			.stream()
			.flatMap(it -> Arrays.stream(it.split(", ?")))
			.filter(it -> !it.equals("vac")) // TODO: Handle vacations
			.map(this::parseDate)
			.collect(Collectors.toSet());
		AppointmentModel.Builder builder = new AppointmentModel.Builder(name, IdGenerator.MISSING_ID);
		
		// TODO: Show SWS and details as tooltip
		
		if (startDate.isPresent() && endDate.isPresent()) {
			builder
				.startDate(startDate.unwrap())
				.lastDate(startDate.unwrap())
				.recurrenceEnd(endDate.unwrap())
				.ignoreDate(false);
		} else {
			builder.ignoreDate(true);
		}
		
		if (startTime.isPresent() && endTime.isPresent()) {
			builder
				.startTime(startTime.unwrap())
				.endTime(endTime.unwrap())
				.ignoreTime(false);
		} else {
			builder.ignoreTime(true);
		}
		
		room.map(Location::new).ifPresent(builder::location);
		repeat.ifPresent(builder::recurrence);
		builder.excludes(excludes);
		
		return new UnivISQueryOutputNode.Leaf(builder.build());
	}
	
	private void resolveParentPath(Node node, Document document, List<String> mutablePath) {
		Option<Node> parentRefNode;
		
		switch (node.getNodeName()) {
			case "Event":
				parentRefNode = childByPath(node, "dbref", "UnivISRef");
				break;
			case "Lecture":
				parentRefNode = childByPath(node, "classification", "UnivISRef")
					.or(() -> childByPath(node, "parent-lv", "UnivISRef"));
				break;
			case "Title":
				parentRefNode = childByPath(node, "parent-title", "UnivISRef");
				break;
			default:
				parentRefNode = Option.empty();
				break;
		}
		
		parentRefNode.ifPresent(refNode -> 
			resolveUnivISRef(refNode, document).ifPresent(child -> {
				Option<String> name = childValueByPath(child, "title").or(() -> childValueByPath(child, "name"));
				name.ifPresent(it -> {
					// Do not add nested lectures to the path
					if (!attributeValue(refNode, "type").filter(t -> t.equals("Lecture")).isPresent()) {
						mutablePath.add(0, it);
					}
					resolveParentPath(child, document, mutablePath);
				});
			})
		);
	}
	
	private Option<Node> resolveUnivISRef(Node refNode, Document document) {
		Option<String> parentType = attributeValue(refNode, "type");
		Option<String> parentKey = attributeValue(refNode, "key");
		
		if (parentType.isPresent() && parentKey.isPresent()) {
			String type = parentType.unwrap();
			String key = parentKey.unwrap();
			
			return Option.of(streamNodes(document.getElementsByTagName(type))
				.filter(it -> matchesKey(it, key))
				.findAny());
		} else {
			return Option.empty();
		}
	}
	
	private boolean matchesKey(Node node, String key) {
		if (node == null) {
			return false;
		} else {
			return attributeValue(node, "key").filter(it -> it.equals(key)).isPresent();
		}
	}
	
	private LocalDate parseDate(String date) {
		return LocalDate.from(DATE_PARSER.parse(date));
	}
	
	private LocalTime parseTime(String date) {
		return LocalTime.from(TIME_PARSER.parse(date));
	}
	
	private Option<String> attributeValue(Node node, String key) {
		return Option.ofNullable(node.getAttributes())
			.flatMap(it -> Option.ofNullable(it.getNamedItem(key)))
			.flatMap(it -> Option.ofNullable(it.getNodeValue()));
	}
	
	private Option<String> childValueByPath(Node node, String... childPath) {
		return childByPath(node, childPath)
			.flatMap(it -> Option.ofNullable(it.getTextContent()));
	}
	
	private Option<Node> childByPath(Node node, String... childPath) {
		return Option.of(childsByPath(node, childPath).findAny());
	}
	
	private Stream<Node> childsByPath(Node node, String... childPath) {
		return childsByPath(node, 0, childPath);
	}
	
	private Stream<Node> childsByPath(Node node, int pathIndex, String... childPath) {
		if (pathIndex >= childPath.length) {
			return Stream.of(node);
		} else {
			return streamNodes(node.getChildNodes())
				.filter(it -> Objects.equals(it.getNodeName(), childPath[pathIndex]))
				.flatMap(it -> childsByPath(it, pathIndex + 1, childPath));
		}
	}
	
	private Stream<Node> streamNodes(NodeList nodes) {
		Stream.Builder<Node> nodeStream = Stream.builder();
		int nodeCount = nodes.getLength();
		
		for (int i = 0; i < nodeCount; i++) {
			nodeStream.accept(nodes.item(i));
		}
		
		return nodeStream.build();
	}
}
