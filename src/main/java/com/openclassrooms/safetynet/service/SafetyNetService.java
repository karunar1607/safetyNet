package com.openclassrooms.safetynet.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SafetyNetService {
	private SafetyNetRepository safetyNetRepository;
	Logger logger = LoggerFactory.getLogger(SafetyNetService.class);

	@Autowired
	public SafetyNetService(SafetyNetRepository safetyNetRepository) {
		super();
		this.safetyNetRepository = safetyNetRepository;
	}

	public PersonSummary getPeopleByStationId(String StationNumber) throws IOException {
		logger.debug("getPeopleByStationId called with station number: " + StationNumber);

		List<String> fireStationAddresses = safetyNetRepository.getAddressByStationNumber(StationNumber);
		logger.debug("getAddressByStationNumber responded with" + fireStationAddresses.toString());
		List<Person> personsByAddress = safetyNetRepository.getPersonsByAddress(fireStationAddresses);

		return safetyNetRepository.getPersonSummary(personsByAddress);

	}

	public List<String> getPhoneByStationId(String stationNumber) throws IOException {
		logger.debug("getPhoneByStationId called with station number: " + stationNumber);

		List<String> fireStationAddresses = safetyNetRepository.getAddressByStationNumber(stationNumber);
		return safetyNetRepository.getPhoneByAddress(fireStationAddresses);

	}

	public List<PersonAddress> getPeopleByAddress(String address) throws IOException {
		logger.debug("getPeopleByAddress called with address: " + address);

		List<String> addresses = new ArrayList<String>();
		addresses.add(address);
		List<Person> persons = safetyNetRepository.getPersonsByAddress(addresses);

		return safetyNetRepository.getMedicalDetails(persons);

	}

	public List<String> getEmailByCity(String city) throws IOException {
		logger.debug("getEmailByCity called with address: " + city);

		return safetyNetRepository.getEmailbyCity(city);

	}

	public List<PersonDetails> getPeopleByStationIds(List<String> Stationnumber) throws IOException {
		logger.debug("getPeopleByStationIds called with Stationnumbers: " + Stationnumber.toArray().toString());

		List<String> fireStationAddresses = safetyNetRepository.getAddressByStationNumbers(Stationnumber);
		return safetyNetRepository.getPersonsByStationIds(fireStationAddresses);

	}

	public List<PersonAddressSet> getPeopleByName(String firstName, String lastName) throws IOException {
		logger.debug("getPeopleByName called with firstname:{} and Last name: {}", firstName, lastName);

		List<Person> persons = safetyNetRepository.getPersonsByName(firstName, lastName);
		return safetyNetRepository.getMedicalDetailswithEmail(persons);

	}

	public ChildSummary getChildByAddress(String address) throws IOException {
		logger.debug("getChildByAddress called with address: " + address);

		List<String> addresses = new ArrayList<String>();
		addresses.add(address);
		List<Person> personsByAddress = safetyNetRepository.getPersonsByAddress(addresses);
		return safetyNetRepository.getChildSummary(personsByAddress);

	}

	public List<Person> createPerson(Person person) throws IOException {
		logger.debug("createPerson called with  with firstname:{} and Last name: {}", person.getFirstName(),
				person.getLastName());

		if (person.getFirstName() == null || person.getLastName() == null) {
			logger.error("createPerson is called with first name or last name as null");
			List<Person> input = new ArrayList<Person>();
			input.add(person);
			return input;

		} else if (person.getFirstName().trim().isBlank() || person.getLastName().trim().isBlank()) {
			logger.error("createPerson is called with first name or last name as emphty");
			List<Person> input = new ArrayList<Person>();
			input.add(person);
			return input;
		} else {
			List<Person> existingPerson = safetyNetRepository.getPersonsByName(person.firstName, person.lastName);
			if (existingPerson.isEmpty()) {
				return safetyNetRepository.createPerson(person);
			}

			logger.warn("createPerson is called with existing person");
			return existingPerson;
		}
	}

	public List<Person> deletePerson(String firstName, String lastName) throws IOException {
		logger.debug("deletePerson called  with firstname:{} and Last name: {}", firstName, lastName);

		List<Person> existingPerson = safetyNetRepository.getPersonsByName(firstName, lastName);
		if (!existingPerson.isEmpty()) {
			return safetyNetRepository.deletePerson(existingPerson.get(0));
		}
		logger.warn("deletePerson is called with non existing person");

		return existingPerson;
	}

	public List<Person> updatePerson(Person person) throws IOException {
		logger.debug("updatePerson called with  with firstname:{} and Last name: {}", person.getFirstName(),
				person.getLastName());

		List<Person> existingPerson = safetyNetRepository.getPersonsByName(person.firstName, person.lastName);
		if (!existingPerson.isEmpty()) {
			return safetyNetRepository.updatePerson(person);
		}
		logger.warn("deletePerson is called with non existing person");
		return existingPerson;
	}

	public List<MedicalRecord> createMedicalRecord(MedicalRecords medicalRecords) {
		logger.debug("createMedicalRecord called with  with firstname:{} and Last name: {}",
				medicalRecords.getFirstName(), medicalRecords.getLastName());
		if (medicalRecords.getFirstName() == null || medicalRecords.getLastName() == null) {
			logger.error("medicalRecords is called with first name or last name as null");
			List<MedicalRecord> input = convertMedicalRecord(medicalRecords);
			return input;

		} else if (medicalRecords.getFirstName().trim().isBlank() || medicalRecords.getLastName().trim().isBlank()) {
			logger.error("medicalRecords is called with first name or last name as empty");
			List<MedicalRecord> input = convertMedicalRecord(medicalRecords);
			return input;

		} else {
			logger.debug("createMedicalRecord called with Person: " + medicalRecords.getFirstName());
			List<MedicalRecord> existingRecord = safetyNetRepository.getMedicalRecord(medicalRecords.getFirstName(),
					medicalRecords.getLastName());
			if (existingRecord.isEmpty()) {
				return safetyNetRepository.createMedicalRecord(medicalRecords);
			}
			logger.warn("createMedicalRecord is called with existing record");

			return existingRecord;

		}

	}

	public List<MedicalRecord> updateMedicalRecord(MedicalRecords medicalRecords) {
		logger.debug("updateMedicalRecord called with  with firstname:{} and Last name: {}",
				medicalRecords.getFirstName(), medicalRecords.getLastName());

		List<MedicalRecord> existingRecord = safetyNetRepository.getMedicalRecord(medicalRecords.getFirstName(),
				medicalRecords.getLastName());
		if (!existingRecord.isEmpty()) {
			return safetyNetRepository.updateMedicalRecord(medicalRecords);
		}
		logger.warn("updateMedicalRecord is called with non existing record");

		return existingRecord;

	}

	public List<MedicalRecord> deleteMedicalRecord(String firstName, String lastName) {
		logger.debug("deleteMedicalRecord called  with firstname:{} and Last name: {}", firstName, lastName);

		List<MedicalRecord> existingRecord = safetyNetRepository.getMedicalRecord(firstName, lastName);
		if (!existingRecord.isEmpty()) {
			return safetyNetRepository.deleteMedicalRecord(existingRecord.get(0));
		}
		logger.warn("deleteMedicalRecord is called with non existing record");

		return existingRecord;

	}

	public List<FireStation> createFireStation(FireStation fireStation) {
		logger.debug("createFireStation called with station number: " + fireStation.getStationNumber());
		if (fireStation.getStationNumber() == null || fireStation.getAddresses() == null) {
			logger.error("createFireStation is called with station number or address as null");
			List<FireStation> fireStationList = new ArrayList<FireStation>();
			fireStationList.add(fireStation);
			return fireStationList;

		} else if (fireStation.getStationNumber().trim().isBlank() || fireStation.getAddresses().isEmpty()) {
			logger.error("createFireStation is called with station number or address as empty");
			List<FireStation> fireStationList = new ArrayList<FireStation>();
			fireStationList.add(fireStation);
			return fireStationList;

		} else {
			List<FireStation> existingRecord = safetyNetRepository.getFireStation(fireStation.getStationNumber());
			if (existingRecord.isEmpty()) {
				return safetyNetRepository.createFireStation(fireStation);
			}
			logger.warn("createFireStation is called with  existing record");
			return existingRecord;

		}
	}

	public List<FireStation> updateFireStation(FireStation fireStation) {
		logger.debug("updateFireStation called with station number: " + fireStation.getStationNumber());

		List<FireStation> existingRecord = safetyNetRepository.getFireStation(fireStation.getStationNumber());
		if (!existingRecord.isEmpty()) {
			return safetyNetRepository.updateFireStation(fireStation);
		}
		logger.warn("updateFireStation is called with  non existing record");

		return existingRecord;
	}

	public List<FireStation> deleteFireStation(String stationNumber) {
		logger.debug("deleteFireStation called with station number: " + stationNumber);

		List<FireStation> existingRecord = safetyNetRepository.getFireStation(stationNumber);
		if (!existingRecord.isEmpty()) {
			return safetyNetRepository.deleteFireStation(existingRecord.get(0));
		}
		logger.warn("deleteFireStation is called with  non existing record");

		return existingRecord;
	}

	public List<MedicalRecord> convertMedicalRecord(MedicalRecords medicalRecord) {
		List<MedicalRecord> medRecodList = new ArrayList<MedicalRecord>();
		MedicalRecord medRec = new MedicalRecord();
		medRec.setFirstName(medicalRecord.getFirstName());
		medRec.setLastName(medicalRecord.getLastName());
		medRec.setMedications(medicalRecord.getMedications());
		medRec.setAllergies(medicalRecord.getAllergies());
		medRec.setAge(calculateAge(medicalRecord.getBirthdate()));
		medRecodList.add(medRec);
		return medRecodList;
	}

	public static int calculateAge(String birthDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
		LocalDate formatedDate = LocalDate.parse(birthDate, formatter);
		LocalDate currentDate = LocalDate.now();
		int period = Period.between(formatedDate, currentDate).getYears();
		return period;

	}

}
