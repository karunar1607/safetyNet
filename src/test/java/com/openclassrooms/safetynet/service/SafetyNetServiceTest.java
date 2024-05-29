package com.openclassrooms.safetynet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import com.openclassrooms.safetynet.domain.ChildSummary;
import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.MedicalRecords;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.domain.PersonAddress;
import com.openclassrooms.safetynet.domain.PersonAddressSet;
import com.openclassrooms.safetynet.domain.PersonDetails;
import com.openclassrooms.safetynet.domain.PersonSummary;
import com.openclassrooms.safetynet.repository.SafetyNetRepository;

@Service
class SafetyNetServiceTest {
	SafetyNetRepository safetyNetRepository = new SafetyNetRepository();
	SafetyNetService safetyNetService = new SafetyNetService(safetyNetRepository);

	@BeforeEach
	void setup() throws IOException {
		safetyNetRepository.loadFirestation();
		safetyNetRepository.loadMedicalRecords();
		safetyNetRepository.loadPersons();
	}

	@Test
	@DisplayName("getPeopleByStationId with valid Station Number")
	void testGetPeopleByStationId_valid() throws IOException {
		// Arrange
		String StationNumber = "1";

		// Act

		PersonSummary ps = safetyNetService.getPeopleByStationId(StationNumber);
		// Assert
		assertEquals(6, ps.getPersonList().size());
		assertEquals(5, ps.getNumOfAdults());
	}

	@Test
	@DisplayName("getPeopleByStationId with invalid Station Number")
	void testGetPeopleByStationId_invalid() throws IOException {
		// Arrange
		String StationNumber = "10";
		// Act

		PersonSummary ps = safetyNetService.getPeopleByStationId(StationNumber);
		// Assert
		assertEquals(0, ps.getPersonList().size());
		assertEquals(0, ps.getNumOfAdults());
	}

	@Test
	@DisplayName("getPhoneByStationId with valid Station Number")

	void testGetPhoneByStationId_valid() throws IOException {
		// Arrange
		String StationNumber = "1";
		// Act
		List<String> phoneNumbers = safetyNetService.getPhoneByStationId(StationNumber);
		// Assert
		assertEquals(6, phoneNumbers.size());
	}

	@Test
	@DisplayName("getPhoneByStationId with invalid Station Number")
	void testGetPhoneByStationId_invalid() throws IOException {
		// Arrange
		String StationNumber = "10";
		// Act
		List<String> phoneNumbers = safetyNetService.getPhoneByStationId(StationNumber);
		// Assert
		assertEquals(0, phoneNumbers.size());
	}

	@Test
	@DisplayName("getPeopleByAddress with valid address")
	void testGetPeopleByAddress_valid() throws IOException {
		// Arrange
		String address = "1509 Culver St";
		// Act
		List<PersonAddress> pa = safetyNetService.getPeopleByAddress(address);
		// Assert
		assertEquals(5, pa.size());
	}

	@Test
	@DisplayName("getPeopleByAddress with invalid address")
	void testGetPeopleByAddress_invalid() throws IOException {
		// Arrange
		String address = "dummy street";
		// Act
		List<PersonAddress> pa = safetyNetService.getPeopleByAddress(address);
		// Assert
		assertEquals(0, pa.size());
	}

	@Test
	@DisplayName("getEmailByCity with valid city")
	void testGetEmailByCity_valid() throws IOException {
		// Arrange
		String city = "Culver";
		// Act
		List<String> emails = safetyNetService.getEmailByCity(city);
		// Assert
		assertEquals(23, emails.size());
	}

	@Test
	@DisplayName("getEmailByCity with invalid city")
	void testGetEmailByCity_invalid() throws IOException {
		// Arrange
		String city = "city";
		// Act
		List<String> emails = safetyNetService.getEmailByCity(city);
		// Assert
		assertEquals(0, emails.size());
	}

	@Test
	@DisplayName("getPeopleByStationIds with valid station numbers")
	void testGetPeopleByStationIds_valid() throws IOException {
		// Arrange
		List<String> Stationnumber = new ArrayList<String>();
		Stationnumber.add("1");
		Stationnumber.add("3");
		// Act
		List<PersonDetails> persDetails = safetyNetService.getPeopleByStationIds(Stationnumber);
		// Assert
		assertEquals(7, persDetails.size());
		assertEquals("Reginold", persDetails.get(0).getPersonAddress().get(0).getFirstName());

	}

	@Test
	@DisplayName("getPeopleByStationIds with invalid station numbers")
	void testGetPeopleByStationIds_invalid() throws IOException {
		// Arrange
		List<String> Stationnumber = new ArrayList<String>();
		Stationnumber.add("8");
		Stationnumber.add("9");
		// Act
		List<PersonDetails> persDetails = safetyNetService.getPeopleByStationIds(Stationnumber);
		// Assert
		assertEquals(0, persDetails.size());

	}

	@Test
	@DisplayName("getPeopleByName with valid name")

	void testGetPeopleByName_valid() throws IOException {
		// Arrange
		String firstName = "Brian";
		String lastName = "Stelzer";
		// Act
		List<PersonAddressSet> persAdd = safetyNetService.getPeopleByName(firstName, lastName);
		// Assert
		assertEquals(1, persAdd.size());
		assertEquals(firstName, persAdd.get(0).getFirstName());
	}

	@Test
	@DisplayName("getPeopleByName with invalid name")
	void testGetPeopleByName_invalid() throws IOException {
		// Arrange
		String firstName = "Who";
		String lastName = "amI";
		// Act
		List<PersonAddressSet> persAdd = safetyNetService.getPeopleByName(firstName, lastName);
		// Asserts
		assertEquals(0, persAdd.size());
	}

	@Test
	@DisplayName("getChildByAddress with valid address")
	void testGetChildByAddress_valid() throws IOException {
		// Arrange
		String address = "1509 Culver St";
		// Act
		ChildSummary summary = safetyNetService.getChildByAddress(address);
		// Asserts
		assertEquals(2, summary.getChild().size());
		assertEquals("Tenley", summary.getChild().get(0).getFirstName());

	}

	@Test
	@DisplayName("getChildByAddress with invalid address")
	void testGetChildByAddress_invalid() throws IOException {
		// Arrange
		String address = "dummy";
		// Act
		ChildSummary summary = safetyNetService.getChildByAddress(address);
		// Asserts
		assertNull(summary.getChild());

	}

	@Test
	@DisplayName("createPerson with new person")
	void testCreatePerson_new() throws IOException {
		// Arrange
		Person person = new Person.PersonBuilder().firstName("karuna").lastName("last").phone("6785674325").zip("2345")
				.address("address").city("city").email("test@test.com").build();
		// Act
		List<Person> persList = safetyNetService.createPerson(person);
		// Asserts
		assertEquals(24, persList.size());
		assertEquals("karuna", persList.get(23).getFirstName());

	}

	@Test
	@DisplayName("createPerson with existing person")
	void testCreatePerson_existing() throws IOException {
		// Arrange
		Person person = new Person.PersonBuilder().firstName("John").lastName("Boyd").phone("6785674325").zip("2345")
				.address("address").city("city").email("test@test.com").build();
		// Act
		List<Person> persList = safetyNetService.createPerson(person);
		// Asserts
		assertEquals(1, persList.size());

	}

	@Test
	@DisplayName("deletePerson with existing person")

	void testDeletePerson_existing() throws IOException {
		// Arrange
		String firstName = "Brian";
		String lastName = "Stelzer";
		// Act
		List<Person> person = safetyNetService.deletePerson(firstName, lastName);
		// Assert
		assertEquals(22, person.size());
	}

	@Test
	@DisplayName("deletePerson with non existing person")
	void testDeletePerson_invalid() throws IOException {
		// Arrange
		String firstName = "Kye";
		String lastName = "Gal";
		// Act
		List<Person> person = safetyNetService.deletePerson(firstName, lastName);
		// Assert
		assertEquals(0, person.size());
	}

	@Test
	@DisplayName("updatePerson with existing person")
	void testUpdatePerson_existing() throws IOException {
		// Arrange
		Person person = new Person.PersonBuilder().firstName("John").lastName("Boyd").phone("6785674325").zip("2345")
				.address("address").city("city").email("test@test.com").build();
		// Act
		List<Person> persList = safetyNetService.updatePerson(person);
		// Asserts
		assertEquals(23, persList.size());
		assertEquals("6785674325", persList.get(0).getPhone());

	}

	@Test
	@DisplayName("updatePerson with non existing person")
	void testUpdatePerson_invalid() throws IOException {
		// Arrange
		Person person = new Person.PersonBuilder().firstName("who").lastName("amI").phone("6785674325").zip("2345")
				.address("address").city("city").email("test@test.com").build();
		// Act
		List<Person> persList = safetyNetService.updatePerson(person);
		// Asserts
		assertEquals(0, persList.size());

	}

	@Test
	@DisplayName("createMedicalRecord with new record")
	void testCreateMedicalRecord_new() {
		// Arrange
		String firstName = "karuna";
		String lastName = "last";
		String phoneNumber = "2345673456";
		String birthdate = "03/06/1999";
		List<String> medications = new ArrayList<String>();
		medications.add("tylenol:250mg");
		List<String> allergies = new ArrayList<String>();
		allergies.add("nillacilan");
		MedicalRecords mrs = new MedicalRecords(firstName, lastName, phoneNumber, birthdate, medications, allergies);

		// Act
		List<MedicalRecord> medRecord = safetyNetService.createMedicalRecord(mrs);
		// Asserts
		assertEquals(24, medRecord.size());

	}

	@Test
	@DisplayName("createMedicalRecord with existing record")
	void testCreateMedicalRecord_existing() {
		// Arrange
		String firstName = "John";
		String lastName = "Boyd";
		String phoneNumber = "2345673456";
		String birthdate = "03/06/1999";
		List<String> medications = new ArrayList<String>();
		medications.add("tylenol:250mg");
		List<String> allergies = new ArrayList<String>();
		allergies.add("nillacilan");
		MedicalRecords mrs = new MedicalRecords(firstName, lastName, phoneNumber, birthdate, medications, allergies);

		// Act
		List<MedicalRecord> medRecord = safetyNetService.createMedicalRecord(mrs);
		// Asserts
		assertEquals(1, medRecord.size());

	}

	@Test
	@DisplayName("updateMedicalRecord with existing record")
	void testUpdateMedicalRecord_existing() {
		// Arrange
		String firstName = "John";
		String lastName = "Boyd";
		String phoneNumber = "2345673456";
		String birthdate = "03/06/1999";
		List<String> medications = new ArrayList<String>();
		medications.add("tylenol:250mg");
		List<String> allergies = new ArrayList<String>();
		allergies.add("pollen");
		MedicalRecords mrs = new MedicalRecords(firstName, lastName, phoneNumber, birthdate, medications, allergies);

		// Act
		List<MedicalRecord> medRecord = safetyNetService.updateMedicalRecord(mrs);
		// Asserts
		assertEquals(23, medRecord.size());
	}

	@Test
	@DisplayName("updateMedicalRecord with non existing record")
	void testUpdateMedicalRecord_invalid() {
		// Arrange
		String firstName = "user";
		String lastName = "test";
		String phoneNumber = "2345673456";
		String birthdate = "03/06/1999";
		List<String> medications = new ArrayList<String>();
		medications.add("tylenol:250mg");
		List<String> allergies = new ArrayList<String>();
		allergies.add("pollen");
		MedicalRecords mrs = new MedicalRecords(firstName, lastName, phoneNumber, birthdate, medications, allergies);

		// Act
		List<MedicalRecord> medRecord = safetyNetService.updateMedicalRecord(mrs);
		// Asserts
		assertEquals(0, medRecord.size());
	}

	@Test
	@DisplayName("deleteMedicalRecord with  existing record")

	void testDeleteMedicalRecord_existing() {
		// Arrange
		String firstName = "Brian";
		String lastName = "Stelzer";
		// Act
		List<MedicalRecord> medRecords = safetyNetService.deleteMedicalRecord(firstName, lastName);
		// Assert
		assertEquals(22, medRecords.size());
	}

	@Test
	@DisplayName("deleteMedicalRecord with non existing record")
	void testDeleteMedicalRecord_invalid() {
		// Arrange
		String firstName = "test";
		String lastName = "user";
		// Act
		List<MedicalRecord> medRecords = safetyNetService.deleteMedicalRecord(firstName, lastName);
		// Assert
		assertEquals(0, medRecords.size());
	}

	@Test
	@DisplayName("createFireStation with new record")
	void testCreateFireStation_new() {
		// Arrange
		String stationNumber = "5";
		Set<String> addresses = new HashSet<String>();
		addresses.add("280 Gailic St");
		FireStation fireStation = new FireStation(addresses, stationNumber);
		// Act
		List<FireStation> fireStations = safetyNetService.createFireStation(fireStation);
		// Assert
		assertEquals(5, fireStations.size());
		assertEquals("5", fireStations.get(4).getStationNumber());

	}

	@Test
	@DisplayName("createFireStation with existing record")
	void testCreateFireStation_existing() {
		// Arrange
		String stationNumber = "1";
		Set<String> addresses = new HashSet<String>();
		addresses.add("280 Gailic St");
		FireStation fireStation = new FireStation(addresses, stationNumber);
		// Act
		List<FireStation> fireStations = safetyNetService.createFireStation(fireStation);
		// Assert
		assertEquals(1, fireStations.size());
	}

	@Test
	@DisplayName("updateFireStation with existing record")
	void testUpdateFireStation_existing() {
		// Arrange
		String stationNumber = "1";
		Set<String> addresses = new HashSet<String>();
		addresses.add("280 Gailic St");
		FireStation fireStation = new FireStation(addresses, stationNumber);
		// Act
		List<FireStation> fireStations = safetyNetService.updateFireStation(fireStation);
		// Assert
		assertEquals(4, fireStations.size());
	}

	@Test
	@DisplayName("updateFireStation with non existing record")
	void testUpdateFireStation_invalid() {
		// Arrange
		String stationNumber = "11";
		Set<String> addresses = new HashSet<String>();
		addresses.add("280 Gailic St");
		FireStation fireStation = new FireStation(addresses, stationNumber);
		// Act
		List<FireStation> fireStations = safetyNetService.updateFireStation(fireStation);
		// Assert
		assertEquals(0, fireStations.size());
	}

	@Test
	@DisplayName("deleteFireStation with  existing record")
	void testDeleteFireStation_existing() {
		// Arrange
		String stationNumber = "1";

		// Act
		List<FireStation> fireStations = safetyNetService.deleteFireStation(stationNumber);
		// Assert
		assertEquals(3, fireStations.size());
	}

	@Test
	@DisplayName("deleteFireStation with  non existing record")
	void testDeleteFireStation_invalid() {
		// Arrange
		String stationNumber = "11";

		// Act
		List<FireStation> fireStations = safetyNetService.deleteFireStation(stationNumber);
		// Assert
		assertEquals(0, fireStations.size());
	}

}
