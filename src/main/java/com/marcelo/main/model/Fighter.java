package com.marcelo.main.model;

import java.util.ArrayList;
import java.util.List;

public class Fighter {
	private Corner corner;
	private String name;
	private String cartel;
	private String country;
	private String age;
	private String height;
	private String weight;
	private List<String> fightHistory = new ArrayList<>();
	
	public Fighter() {
		super();
	}

	public Fighter(Corner corner, String name, String cartel, String country, String age, String height, String weight,
			List<String> fightHistory) {
		super();
		this.corner = corner;
		this.name = name;
		this.cartel = cartel;
		this.country = country;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.fightHistory = fightHistory;
	}

	public Corner getCorner() {
		return corner;
	}

	public void setCorner(Corner corner) {
		this.corner = corner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCartel() {
		return cartel;
	}

	public void setCartel(String cartel) {
		this.cartel = cartel;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public List<String> getFightHistory() {
		return fightHistory;
	}

	public void addFightHistory(String fight) {
		fightHistory.add(fight);
	}
	
	
}
