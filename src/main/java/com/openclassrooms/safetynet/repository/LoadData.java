package com.openclassrooms.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.Person;

import jakarta.annotation.PostConstruct;

@Component
public class LoadData {

	public List<Person> persons = new ArrayList<Person>();
	public List<MedicalRecord> medRecodList = new ArrayList<MedicalRecord>();
	public List<FireStation> fireStations = new ArrayList<FireStation>();

	public List<Person> getPersons() {
		return this.persons;
	}

	public List<MedicalRecord> getMedRecodList() {
		return this.medRecodList;
	}

	public List<FireStation> getFireStations() {
		return this.fireStations;
	}

	@PostConstruct
	public void loadPersons() throws IOException {
		String filePath = "src/main/resources/safetynetdata.json";
		byte[] bytesFile = Files.readAllBytes(new File(filePath).toPath());
		JsonIterator iter = JsonIterator.parse(bytesFile);
		Any any = iter.readAny();
		Any personAny = any.get("persons");

		personAny.forEach(a -> persons.add(new Person.PersonBuilder().firstName(a.get("firstName").toString())
				.address(a.get("address").toString()).city(a.get("city").toString())
				.lastName(a.get("lastName").toString()).phone(a.get("phone").toString()).zip(a.get("zip").toString())
				.email(a.get("email").toString()).build()));

	}

	@PostConstruct
	public void loadMedicalRecords() throws IOException {

		String filePath = "src/main/resources/safetynetdata.json";
		byte[] bytesFile = Files.readAllBytes(new File(filePath).toPath());

		JsonIterator iter = JsonIterator.parse(bytesFile);
		Any any = iter.readAny();
		Any medAny = any.get("medicalrecords");
		medAny.forEach(medicalRecord -> {
			MedicalRecord medRecord = new MedicalRecord();

			medRecord.setFirstName(medicalRecord.get("firstName").toString());
			medRecord.setLastName(medicalRecord.get("lastName").toString());

			String dateConvert = medicalRecord.get("birthdate").toString();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);

			LocalDate birthDate = LocalDate.parse(dateConvert, formatter);
			LocalDate currentDate = LocalDate.now();
			int period = Period.between(birthDate, currentDate).getYears();
			medRecord.setAge(period);
			Any medications = medicalRecord.get("medications");
			List<String> medication = new ArrayList<String>();

			medications.forEach(a -> {
				medication.add(a.toString());

			});
			medRecord.setMedications(medication);
			Any allergies = medicalRecord.get("allergies");
			List<String> allergie = new ArrayList<String>();
			allergies.forEach(a -> {
				allergie.add(a.toString());

			});
			medRecord.setAllergies(allergie);
			medRecodList.add(medRecord);
		});
	}

	@PostConstruct
	public void loadFirestation() throws IOException {

		String filePath = "src/main/resources/safetynetdata.json";
		byte[] bytesFile = Files.readAllBytes(new File(filePath).toPath());

		JsonIterator iter = JsonIterator.parse(bytesFile);
		Any any = iter.readAny();
		Map<String, FireStation> fireStationMap = new HashMap<>();
		Any fireStationAny = any.get("firestations");
		fireStationAny.forEach(anyStation -> {
			fireStationMap.compute(anyStation.get("station").toString(),
					(k, v) -> v == null
							? new FireStation(anyStation.get("station").toString())
									.addAddress(anyStation.get("address").toString())
							: v.addAddress(anyStation.get("address").toString()));
		});

		fireStations = fireStationMap.values().stream().collect(Collectors.toList());
	}

}
