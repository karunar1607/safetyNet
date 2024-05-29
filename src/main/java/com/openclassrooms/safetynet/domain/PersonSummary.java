package com.openclassrooms.safetynet.domain;

import java.util.List;

public class PersonSummary {
	List<PersonFirestation> personList;
	int numOfAdults;
	int numOfChild;
	public List<PersonFirestation> getPersonList() {
		return personList;
	}
	public void setPersonList(List<PersonFirestation> personList) {
		this.personList = personList;
	}
	public int getNumOfAdults() {
		return numOfAdults;
	}
	public void setNumOfAdults(int numOfAdults) {
		this.numOfAdults = numOfAdults;
	}
	public int getNumOfChild() {
		return numOfChild;
	}
	public void setNumOfChild(int numOfChild) {
		this.numOfChild = numOfChild;
	}
	
	
	
}
