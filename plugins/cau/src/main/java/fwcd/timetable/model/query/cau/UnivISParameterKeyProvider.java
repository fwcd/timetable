package fwcd.timetable.model.query.cau;

import java.util.Arrays;
import java.util.List;

import fwcd.fructose.Lazy;
import fwcd.timetable.model.query.ParameterKey;
import fwcd.timetable.model.query.ParameterValue;

public class UnivISParameterKeyProvider {
	private final Lazy<List<ParameterKey>> keys = Lazy.of(() -> Arrays.asList(
		new ParameterKey.Builder("search")
			.label("Suche")
			.values(
				new ParameterValue.Builder("lectures")
					.label("Vorlesungen")
					.possibleParameters("department", "chapter", "name", "shortname", "number", "sws", "bonus", "malus", "ectscredits", "lecturer", "room", "noimports", "type", "keywords", "path", "id", "evaluation", "allocation")
					.build(),
				new ParameterValue.Builder("persons")
					.label("Personen")
					.possibleParameters("department", "name", "firstname", "fullname", "lehrtyp", "title", "path", "xjob", "id", "ordnumber", "tel", "lehr")
					.build(),
				new ParameterValue.Builder("chapters")
					.label("Ueberschriften")
					.possibleParameters("name", "fullname", "path")
					.build(),
				new ParameterValue.Builder("departments")
					.label("Institutionen")
					.possibleParameters("name", "number", "fullname", "path", "id")
					.build(),
				new ParameterValue.Builder("calendar")
					.label("Kalender")
					.possibleParameters("department", "name", "fullname", "startdate", "enddate", "who", "subtitle", "type", "contributor")
					.build(),
				new ParameterValue.Builder("rooms")
					.label("Raeume")
					.possibleParameters("department", "name", "longname", "contact", "fullname", "size", "description", "showstart", "showend", "path", "id")
					.build(),
				new ParameterValue.Builder("allocations")
					.label("Aktivitaeten in Raeumen")
					.possibleParameters("name", "start", "end", "starttime", "endtime", "contact", "department")
					.build(),
				new ParameterValue.Builder("publications")
					.label("Publikationen")
					.possibleParameters("department", "name", "fullname", "author", "number", "month", "year", "editor", "type", "location", "publisher", "keywords", "booktitle", "id")
					.build(),
				new ParameterValue.Builder("projects")
					.label("Forschungsprojekte")
					.possibleParameters("department", "name", "fullname", "director", "contact"	)
					.build(),
				new ParameterValue.Builder("functions")
					.label("Funktionen")
					.possibleParameters("department", "name", "fullname", "person")
					.build(),
				new ParameterValue.Builder("events")
					.label("Termine")
					.possibleParameters("department", "name", "fullname", "start", "end", "who", "contact", "incalendar", "linked", "subtitle", "type", "contributor", "id")
					.build()
			)
			.build(),
		new ParameterKey.Builder("department")
			.label("Abteilung")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("name")
			.label("Name")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("chapter")
			.label("Kapitel")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("shortname")
			.label("Kurzname")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("sws")
			.label("SWS")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("bonus")
			.label("Bonus")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("malus")
			.label("Malus")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("ectscredits")
			.label("ECTS Credits")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("lecturer")
			.label("Dozent")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("room")
			.label("Raum")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("noimports")
			.label("Keine Verweise?")
			.hidden(true)
			.values(new ParameterValue.Builder("0").label("Nein").build(), new ParameterValue.Builder("1").label("Ja").build())
			.build(),
		new ParameterKey.Builder("type")
			.label("Typ")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("keywords")
			.label("Stichworte")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("path")
			.label("Pfad")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("id")
			.label("ID")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("evaluation")
			.label("Auswertung")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("allocation")
			.label("Allokation")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("firstname")
			.label("Vorname")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("fullname")
			.label("Voller Name")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("lehrtyp")
			.label("Lehrtyp")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("title")
			.label("Titel")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("xjob")
			.label("XJob")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("ordnumber")
			.label("ORD-Nummer")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("tel")
			.label("Telefon")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("lehr")
			.label("Lehrpersonen?")
			.hidden(true)
			.values(new ParameterValue.Builder("0").label("Nein").build(), new ParameterValue.Builder("1").label("Ja").build())
			.build(),
		new ParameterKey.Builder("startdate")
			.label("Anfangdatum")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("enddate")
			.label("Enddatum")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("who")
			.label("Wer")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("subtitle")
			.label("Untertitel")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("contributor")
			.label("Mitwirkender")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("longname")
			.label("Langer Name")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("contact")
			.label("Kontakt")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("size")
			.label("Groesse")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("description")
			.label("Beschreibung")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("showstart")
			.label("Showanfang")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("showend")
			.label("Showende")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("start")
			.label("Anfang")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("end")
			.label("Ende")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("author")
			.label("Autor")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("month")
			.label("Monat")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("year")
			.label("Jahr")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("editor")
			.label("Bearbeiter")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("location")
			.label("Ort")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("publisher")
			.label("Veroeffentlicher")
			.hidden(true)
			.build(),
		new ParameterKey.Builder("director")
			.label("Leiter")
			.hidden(true)
			.build()
		// TODO: Hidden parameters
	));
	
	public List<ParameterKey> getKeys() { return keys.get(); }
}
