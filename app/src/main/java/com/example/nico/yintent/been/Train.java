package com.example.nico.yintent.been;


import java.io.Serializable;
import java.util.Map;

public class Train implements Serializable {
	private String trainNo;
	private String startStationName;
	private String endStationName;
	private String fromStationName;
	private String startTime;
	private String arriveTime;
	private String toStationName;
	private int dayDifference;
	private String durationTime;
	private String startTrainDate;
	private Map<String, Seat> seats;

	public String getTrainNo() {
		return trainNo;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public int getDayDifference() {
		return dayDifference;
	}

	public String getDurationTime() {
		return durationTime;
	}

	public String getEndStationName() {
		return endStationName;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public Map<String, Seat> getSeats() {
		return seats;
	}

	public String getStartStationName() {
		return startStationName;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStartTrainDate() {
		return startTrainDate;
	}

	public String getToStationName() {
		return toStationName;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public void setDayDifference(int dayDifference) {
		this.dayDifference = dayDifference;
	}

	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}

	public void setEndStationName(String endStationName) {
		this.endStationName = endStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public void setSeats(Map<String, Seat> seats) {
		this.seats = seats;
	}

	public void setStartStationName(String startStationName) {
		this.startStationName = startStationName;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setStartTrainDate(String startTrainDate) {
		this.startTrainDate = startTrainDate;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	@Override
	public String toString() {
		return "Train [trainNo=" + trainNo + ", startStationName="
				+ startStationName + ", endStationName=" + endStationName
				+ ", fromStationName=" + fromStationName + ", startTime="
				+ startTime + ", arriveTime=" + arriveTime + ", toStationName="
				+ toStationName + ", dayDifference=" + dayDifference
				+ ", durationTime=" + durationTime + ", startTrainDate="
				+ startTrainDate + ", seats=" + seats + "]";
	}

}
