package fwcd.timetable.model.query.cau;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import fwcd.timetable.model.utils.HttpConnection;
import fwcd.timetable.model.utils.ParseUtils;
import fwcd.timetable.model.utils.RemoteHttpServer;

public class UnivISDepartmentProvider {
	private static final String URL = "http://univis.uni-kiel.de/out/sysadmin-hilfe/orgliste.txt";
	private UnivISDepartmentNode departmentTree;
	
	private UnivISDepartmentNode fetchDepartments() throws IOException {
		UnivISDepartmentNode root = new UnivISDepartmentNode("Abteilungen", "");
		RemoteHttpServer server = new RemoteHttpServer(URL);
		try (HttpConnection connection = server.sendGetRequest()) {
			connection.readLines(StandardCharsets.ISO_8859_1)
				.skip(3) // lines in orgliste.txt
				.forEach(it -> {
					String[] splitted = it.split("\\|");
					String orgNumber = splitted[splitted.length - 1];
					if (ParseUtils.isNumeric(orgNumber)) {
						UnivISDepartmentNode node = root;
						
						for (int i=0; i<splitted.length-1; i++) {
							String childName = splitted[i];
							if (!childName.isEmpty()) {
								node = node.getOrAddSubDepartmentByName(childName, orgNumber);
							}
						}
					}
				});
		}
		return root;
	}
	
	public UnivISDepartmentNode getDepartments() throws IOException {
		if (departmentTree == null) {
			departmentTree = fetchDepartments();
		}
		return departmentTree;
	}
}
