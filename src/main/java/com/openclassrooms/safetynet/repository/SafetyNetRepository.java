package com.openclassrooms.safetynet.repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.AdultDetails;
import com.openclassrooms.safetynet.domain.ChildDetails;
import com.openclassrooms.safetynet.domain.ChildSummary;
import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.MedicalRecords;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.domain.PersonAddress;
import com.openclassrooms.safetynet.domain.PersonAddressSet;
import com.openclassrooms.safetynet.domain.PersonDetails;
import com.openclassrooms.safetynet.domain.PersonFirestation;
import com.openclassrooms.safetynet.domain.PersonSummary;

@Service
public class SafetyNetRepository extends LoadData {

	public List<String> getAddressByStationNumber(String stationNumber) throws IOException {

		List<String> stationAddress = new ArrayList<String>();

		for (FireStation fireStation : fireStations) {
			if (fireStation.getStationNumber().contains(stationNumber)) {

				stationAddress = fireStation.getAddresses().stream().collect(Collectors.toList());
				break;
			}

		}

		return stationAddress;
	}

	public List<Person> getPersonsByAddress(List<String> addresses) throws IOException {

		List<Person> returnPersons = new ArrayList<>();

		for (Person person : persons) {

			for (String add : addresses) {

				if (person.address.contains(add)) {

					returnPersons.add(new Person.PersonBuilder().firstName(person.firstName.toString())
							.address(person.address.toString()).city(person.city.toString())
							.lastName(person.lastName.toString()).phone(person.phone.toString())
							.zip(person.zip.toString()).email(person.email.toString()).build());
				}
			}

		}

		return returnPersons;

	}

	public List<String> getPhoneByAddress(List<String> addresses) throws IOException {

		List<String> returnPhone = new ArrayList<>();

		for (Person person : persons) {
			for (String add : addresses) {
				if (person.address.contains(add)) {
					returnPhone.add(person.phone);
				}
			}

		}

		return returnPhone;

	}

	public List<PersonAddress> getMedicalDetails(List<Person> person) throws IOException {

		List<PersonAddress> returnMedRecodList = new ArrayList<PersonAddress>();

		for (Person pers : person) {
			for (MedicalRecord mr : medRecodList) {

				if (((pers.firstName.trim()).equalsIgnoreCase(mr.getFirstName().trim()))
						&& ((pers.lastName.trim().equalsIgnoreCase(mr.getLastName().trim())))) {
					PersonAddress pMaster = new PersonAddress();
					pMaster.setFirstName(pers.firstName);
					pMaster.setLastName(pers.lastName);
					pMaster.setPhone(pers.phone);
					pMaster.setAge(mr.getAge());
					pMaster.setMedications(mr.getMedications());
					pMaster.setAllergies(mr.getAllergies());
					returnMedRecodList.add(pMaster);
				}
			}

		}

		return returnMedRecodList;

	}

	public PersonSummary getPersonSummary(List<Person> person) throws IOException {

		List<PersonFirestation> returnMedRecodList = new ArrayList<PersonFirestation>();
		PersonSummary personSummary = new PersonSummary();

		for (Person pers : person) {
			for (MedicalRecord mr : medRecodList) {

				if (((pers.firstName.trim()).equalsIgnoreCase(mr.getFirstName().trim()))
						&& ((pers.lastName.trim().equalsIgnoreCase(mr.getLastName().trim())))) {
					PersonFirestation pMaster = new PersonFirestation();
					pMaster.setFirstName(pers.firstName);
					pMaster.setLastName(pers.lastName);
					pMaster.setAddress(pers.address);
					pMaster.setCity(pers.city);
					pMaster.setZip(pers.zip);
					pMaster.setPhone(pers.phone);

					pMaster.setAge(mr.getAge());

					returnMedRecodList.add(pMaster);
				}
			}

		}
		personSummary.setPersonList(returnMedRecodList);
		personSummary.setNumOfAdults(getCount(returnMedRecodList).get(0));
		personSummary.setNumOfChild(getCount(returnMedRecodList).get(1));

		return personSummary;

	}

	public static List<Integer> getCount(List<PersonFirestation> person) {
		List<Integer> returnCount = new ArrayList<Integer>();
		int childCount = 0;
		int adultCount = 0;
		for (PersonFirestation pers : person) {
			if ((pers.getAge()) >= 18) {

				adultCount += 1;
			} else {

				childCount += 1;
			}
		}
		returnCount.add(adultCount);
		returnCount.add(childCount);

		return returnCount;

	}

	public List<String> getEmailbyCity(String city) throws IOException {

		List<String> returnEmail = new ArrayList<>();

		for (Person person : persons) {

			if (person.city.equalsIgnoreCase(city)) {
				returnEmail.add(person.email);
			}

		}

		return returnEmail;

	}

	public List<String> getAddressByStationNumbers(List<String> stationNumbers) throws IOException {

		List<String> stationAddress = new ArrayList<String>();

		for (FireStation fireStation : fireStations) {
			for (String stationNumber : stationNumbers) {
				if (fireStation.getStationNumber().contains(stationNumber)) {

					stationAddress.addAll(fireStation.getAddresses().stream().collect(Collectors.toList()));
					break;
				}

			}

		}
		return stationAddress;

	}

	public List<PersonDetails> getPersonsByStationIds(List<String> addresses) throws IOException {

		List<PersonDetails> returnPersonDetails = new ArrayList<PersonDetails>();

		for (String add : addresses) {
			PersonDetails persDetails = new PersonDetails();
			List<PersonAddress> listPerAdd = new ArrayList<PersonAddress>();
			persDetails.setAddress(add);

			for (Person person : persons) {

				if (person.address.equalsIgnoreCase(add)) {

					persDetails.setAddress(add);
					persDetails.setCity(person.city);
					persDetails.setZip(person.zip);
					PersonAddress persAddress = new PersonAddress();

					persAddress.setFirstName(person.firstName);
					persAddress.setLastName(person.lastName);
					persAddress.setPhone(person.phone);
					for (MedicalRecord mr : medRecodList) {
						if ((person.firstName).equalsIgnoreCase(mr.getFirstName())
								&& ((person.lastName).equalsIgnoreCase(mr.getLastName()))) {
							persAddress.setMedications(mr.getMedications());
							persAddress.setAllergies(mr.getAllergies());
							persAddress.setAge(mr.getAge());
						}

					}

					listPerAdd.add(persAddress);

					persDetails.setPersonAddress(listPerAdd);
				}

			}
			if (persDetails.getPersonAddress() != null) {
				returnPersonDetails.add(persDetails);
			}

		}

		return returnPersonDetails;

	}

	public List<Person> getPersonsByName(String firstName, String lastName) throws IOException {

		List<Person> returnPersons = new ArrayList<>();

		for (Person person : persons) {

			if ((person.firstName).equalsIgnoreCase(firstName.trim())
					&& (person.lastName).equalsIgnoreCase(lastName.trim())) {

				returnPersons.add(new Person.PersonBuilder().firstName(person.firstName.toString())
						.address(person.address.toString()).city(person.city.toString())
						.lastName(person.lastName.toString()).phone(person.phone.toString()).zip(person.zip.toString())
						.email(person.email.toString()).build());
			}
		}

		return returnPersons;

	}

	public List<PersonAddressSet> getMedicalDetailswithEmail(List<Person> person) throws IOException {

		List<PersonAddressSet> returnMedRecodList = new ArrayList<PersonAddressSet>();

		for (Person pers : person) {
			for (MedicalRecord mr : medRecodList) {

				if (((pers.firstName.trim()).equalsIgnoreCase(mr.getFirstName().trim()))
						&& ((pers.lastName.trim().equalsIgnoreCase(mr.getLastName().trim())))) {
					PersonAddressSet pMaster = new PersonAddressSet();
					pMaster.setFirstName(pers.firstName);
					pMaster.setLastName(pers.lastName);
					pMaster.setPhone(pers.phone);
					pMaster.setEmail(pers.email);
					pMaster.setAge(mr.getAge());
					pMaster.setMedications(mr.getMedications());
					pMaster.setAllergies(mr.getAllergies());
					returnMedRecodList.add(pMaster);
				}
			}

		}

		return returnMedRecodList;

	}

	public ChildSummary getChildSummary(List<Person> person) throws IOException {

		List<PersonFirestation> returnMedRecodList = new ArrayList<PersonFirestation>();
		List<AdultDetails> adultList = new ArrayList<AdultDetails>();
		List<ChildDetails> childList = new ArrayList<ChildDetails>();
		ChildSummary childSummary = new ChildSummary();

		boolean hasChild = false;

		for (Person pers : person) {
			for (MedicalRecord mr : medRecodList) {

				if (((pers.firstName.trim()).equalsIgnoreCase(mr.getFirstName().trim()))
						&& ((pers.lastName.trim().equalsIgnoreCase(mr.getLastName().trim())))) {
					PersonFirestation pMaster = new PersonFirestation();
					pMaster.setFirstName(pers.firstName);
					pMaster.setLastName(pers.lastName);
					pMaster.setAddress(pers.address);
					pMaster.setCity(pers.city);
					pMaster.setZip(pers.zip);
					pMaster.setPhone(pers.phone);

					pMaster.setAge(mr.getAge());

					returnMedRecodList.add(pMaster);
				}
			}

		}
		for (PersonFirestation pers : returnMedRecodList) {
			if ((pers.getAge()) < 18) {
				hasChild = true;
				break;
			}
		}

		if (hasChild) {
			for (PersonFirestation pers : returnMedRecodList) {
				if ((pers.getAge()) > 18) {
					AdultDetails adult = new AdultDetails();
					adult.setFirstName(pers.getFirstName());
					adult.setLastName(pers.getLastName());
					adultList.add(adult);

				} else {
					ChildDetails child = new ChildDetails();
					child.setFirstName(pers.getFirstName());
					child.setLastName(pers.getLastName());
					child.setAge(pers.getAge());
					childList.add(child);
				}

				childSummary.setChild(childList);
				childSummary.setOtherAdults(adultList);

			}
		}

		return childSummary;

	}

	public List<FireStation> createFireStation(FireStation fireStation) {

		fireStations.add(fireStation);
		return fireStations;
	}

	public List<Person> createPerson(Person person) {
		persons.add(person);
		return persons;

	}

	public List<MedicalRecord> createMedicalRecord(MedicalRecords medicalRecord) {
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

	public List<Person> updatePerson(Person person) throws IOException {
		for (Person pers : persons) {

			if ((pers.firstName).equalsIgnoreCase(person.firstName)
					&& (pers.lastName).equalsIgnoreCase(person.lastName)) {
				pers.setAddress(person.getAddress());
				pers.setCity(person.getCity());
				pers.setZip(person.getZip());
				pers.setEmail(person.getEmail());
				pers.setPhone(person.getPhone());
			}
		}
		return persons;

	}

	public List<Person> deletePerson(Person person) {

		int index = 0;
		for (Person existPerson : persons) {

			if ((existPerson.firstName).equalsIgnoreCase(person.getFirstName())
					&& (existPerson.lastName).equalsIgnoreCase(person.getLastName())) {

				index = persons.indexOf(existPerson);
			}
		}
		persons.remove(index);

		return persons;
	}

	public List<MedicalRecord> updateMedicalRecord(MedicalRecords medicalRecords) {
		MedicalRecord medRec = new MedicalRecord();
		medRec.setFirstName(medicalRecords.getFirstName());
		medRec.setLastName(medicalRecords.getLastName());
		medRec.setMedications(medicalRecords.getMedications());
		medRec.setAllergies(medicalRecords.getAllergies());
		medRec.setAge(calculateAge(medicalRecords.getBirthdate()));
		for (MedicalRecord mr : medRecodList) {
			if (mr.getFirstName().equalsIgnoreCase(medRec.getFirstName())
					&& mr.getLastName().equalsIgnoreCase(medRec.getLastName())) {
				mr.setAge(medRec.getAge());
				mr.setAllergies(medRec.getAllergies());
				mr.setMedications(medRec.getMedications());
			}

		}
		return medRecodList;

	}

	public List<MedicalRecord> getMedicalRecord(String firstName, String lastName) {
		List<MedicalRecord> returnMedRecords = new ArrayList<MedicalRecord>();

		for (MedicalRecord mr : medRecodList) {
			if (mr.getFirstName().equalsIgnoreCase(firstName) && mr.getLastName().equalsIgnoreCase(lastName)) {
				returnMedRecords.add(mr);

			}

		}

		return returnMedRecords;

	}

	public List<MedicalRecord> deleteMedicalRecord(MedicalRecord medicalRecord) {
		int index = 0;
		for (MedicalRecord mr : medRecodList) {

			if (mr.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
					&& mr.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {

				index = medRecodList.indexOf(mr);
			}
		}
		medRecodList.remove(index);

		return medRecodList;
	}

	public List<FireStation> getFireStation(String stationNumber) {
		List<FireStation> returnFireStation = new ArrayList<FireStation>();
		for (FireStation fr : fireStations) {
			if (fr.getStationNumber().equalsIgnoreCase(stationNumber.trim())) {
				returnFireStation.add(fr);
			}
		}
		return returnFireStation;
	}

	public List<FireStation> updateFireStation(FireStation fireStation) {
		for (FireStation fr : fireStations) {
			if (fr.getStationNumber().equalsIgnoreCase(fireStation.getStationNumber())) {
				fr.setAddresses(fireStation.getAddresses());
			}
		}
		;
		return fireStations;
	}

	public List<FireStation> deleteFireStation(FireStation fireStation) {
		int index = 0;
		for (FireStation fr : fireStations) {
			if (fr.getStationNumber().equalsIgnoreCase(fireStation.getStationNumber())) {
				index = fireStations.indexOf(fr);
			}
			;
		}
		fireStations.remove(index);
		return fireStations;

	}
}
