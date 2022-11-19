package com.marcelo.main.model;

public class Fighter {
	private Corner corner;
	private String name;
	private String cartel;
	private String country;
	private String height;
	private String weight;
	
	public Fighter() {
		super();
	}

	public Fighter(Corner corner, String name, String cartel, String country, String height, String weight) {
		super();
		this.corner = corner;
		this.name = name;
		this.cartel = cartel;
		this.country = country;
		this.height = height;
		this.weight = weight;
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

	
	
	
}
