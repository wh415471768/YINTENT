package com.example.nico.yintent.been;

import java.io.Serializable;

public class Seat implements Serializable {
	private String seatName;
	private int seatNum;
	private double seatPrice;
	private String seatNo;

	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public double getSeatPrice() {
		return seatPrice;
	}

	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	@Override
	public String toString() {
		return "Seat [seatName=" + seatName + ", seatNum=" + seatNum
				+ ", seatPrice=" + seatPrice + ", seatNo=" + seatNo + "]";
	}

}
