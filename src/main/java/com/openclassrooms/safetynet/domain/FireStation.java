package com.openclassrooms.safetynet.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FireStation {
	private Set<String> addresses = new HashSet<>();
	private String stationNumber;

	public FireStation(Set<String> addresses, String stationNumber) {
		super();
		this.addresses = addresses;
		this.stationNumber = stationNumber;
	}

	public void setAddresses(Set<String> addresses) {
		this.addresses = addresses;
	}

	public void setStationNumber(String stationNumber) {
		this.stationNumber = stationNumber;
	}

	public FireStation(String stationNumber) {
		this.stationNumber = stationNumber;
	}

	public FireStation addAddress(String address) {
		addresses.add(address);
		return this;
	}

	public String getStationNumber() {
		return stationNumber;
	}

	public Set<String> getAddresses() {
		if (addresses!=null) {
		return addresses.stream().collect(Collectors.toSet());
		}
		return addresses;
		
	}

	@Override
	public String toString() {
		return stationNumber.concat(": ") + String.join(", ", addresses);
	}

}
