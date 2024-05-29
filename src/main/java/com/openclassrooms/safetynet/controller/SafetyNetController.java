package com.openclassrooms.safetynet.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.safetynet.domain.ChildSummary;
import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.MedicalRecords;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.domain.PersonAddress;
import com.openclassrooms.safetynet.domain.PersonAddressSet;
import com.openclassrooms.safetynet.domain.PersonDetails;
import com.openclassrooms.safetynet.domain.PersonSummary;
import com.openclassrooms.safetynet.service.SafetyNetService;

@RestController
public class SafetyNetController {

	private SafetyNetService safetyNetService;
	Logger logger = LoggerFactory.getLogger(SafetyNetController.class);

	@Autowired
	public SafetyNetController(SafetyNetService safetyNetService) {
		super();
		this.safetyNetService = safetyNetService;
	}

	@GetMapping("/firestation")
	public PersonSummary getPeopleByFireStation(@RequestParam String stationNumber) throws IOException {
		logger.info("GET /firestation called");

		return safetyNetService.getPeopleByStationId(stationNumber);

	}

	@GetMapping("/phoneAlert")
	public List<String> getPhoneByFireStation(@RequestParam String fireStation) throws IOException {
		logger.info("GET /phoneAlert called");
		return safetyNetService.getPhoneByStationId(fireStation);

	}

	@GetMapping("/fire")
	public List<PersonAddress> getPeopleByFireStationAddress(@RequestParam String address) throws IOException {
		logger.info("GET /fire called");
		return safetyNetService.getPeopleByAddress(address);
	}

	@GetMapping("/communityEmail")
	public List<String> getEmailByCity(@RequestParam String city) throws IOException {
		logger.info("GET /communityEmaile called");
		return safetyNetService.getEmailByCity(city);

	}

	@GetMapping("/flood/stations")
	public List<PersonDetails> getPeopleByFireStations(@RequestParam List<String> stations) throws IOException {
		logger.info("GET /flood/station called");
		return safetyNetService.getPeopleByStationIds(stations);

	}

	@GetMapping("/personInfo")
	public List<PersonAddressSet> getPeopleByName(@RequestParam String firstName, @RequestParam String lastName)
			throws IOException {
		logger.info("GET /personInfo called");

		return safetyNetService.getPeopleByName(firstName, lastName);

	}

	@GetMapping("/childAlert")
	public ChildSummary getChildByAddress(@RequestParam String address) throws IOException {
		logger.info("GET /childAlert called");

		return safetyNetService.getChildByAddress(address);

	}

	@PostMapping("/person")
	public List<Person> createPerson(@RequestBody Person person) throws IOException {
		logger.info("POST /person called");
		return safetyNetService.createPerson(person);
	}

	@PutMapping("/person")

	public List<Person> upddatePerson(@RequestBody Person person) throws IOException {
		logger.info("PUT /person called");
		return safetyNetService.updatePerson(person);
	}

	@DeleteMapping("/person")

	public List<Person> deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws IOException {
		logger.info("DELETE /person called");

		return safetyNetService.deletePerson(firstName,lastName);
	}

	@PostMapping("/medicalRecord")
	public List<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecords medicalRecords) {
		logger.info("POST /medicalRecord called");

		return safetyNetService.createMedicalRecord(medicalRecords);

	}

	@PutMapping("/medicalRecord")

	public List<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecords medicalRecords) {
		logger.info("PUT /medicalRecord called");
		return safetyNetService.updateMedicalRecord(medicalRecords);

	}

	@DeleteMapping("/medicalRecord")
	public List<MedicalRecord> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
		logger.info("DELETE /medicalRecord called");
		return safetyNetService.deleteMedicalRecord(firstName,lastName);

	}

	@PostMapping("/firestation")
	public List<FireStation> createFireStation(@RequestBody FireStation fireStation) {
		logger.info("POST /firestation called");

		return safetyNetService.createFireStation(fireStation);

	}

	@PutMapping("/firestation")
	public List<FireStation> updateFireStation(@RequestBody FireStation fireStation) {
		logger.info("PUT /firestation called");

		return safetyNetService.updateFireStation(fireStation);

	}

	@DeleteMapping("/firestation")
	public List<FireStation> deleteFireStation(@RequestParam String stationNumber) {
		logger.info("DELETE /firestation called");

		return safetyNetService.deleteFireStation(stationNumber);

	}
}
