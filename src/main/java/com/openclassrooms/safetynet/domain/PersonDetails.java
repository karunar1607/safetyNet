package com.openclassrooms.safetynet.domain;


import java.util.List;

public class PersonDetails {
	
String address;
String city;
String zip;
List<PersonAddress> personAddress;
public List<PersonAddress> getPersonAddress() {
	return personAddress;
}
public void setPersonAddress(List<PersonAddress> personAddress) {
	this.personAddress = personAddress;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getZip() {
	return zip;
}
public void setZip(String zip) {
	this.zip = zip;
}

	
	

}
