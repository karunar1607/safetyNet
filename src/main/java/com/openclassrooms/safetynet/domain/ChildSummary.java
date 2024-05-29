package com.openclassrooms.safetynet.domain;

import java.util.List;

public class ChildSummary {

	List<ChildDetails> child;
	List<AdultDetails> otherAdults;
	public List<ChildDetails> getChild() {
		return child;
	}
	public void setChild(List<ChildDetails> child) {
		this.child = child;
	}
	public List<AdultDetails> getOtherAdults() {
		return otherAdults;
	}
	public void setOtherAdults(List<AdultDetails> otherAdults) {
		this.otherAdults = otherAdults;
	}
	
}
