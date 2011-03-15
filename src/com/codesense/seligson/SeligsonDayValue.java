package com.codesense.seligson;

import java.util.Date;

public class SeligsonDayValue {
	private Date day;
	private double value;
	
	public SeligsonDayValue(Date day, double value) {
		this.day = day;
		this.value = value;
	}
	
	/**
	 * @param day the day to set
	 */
	public void setDay(Date day) {
		this.day = day;
	}
	/**
	 * @return the day
	 */
	public Date getDay() {
		return day;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
}
