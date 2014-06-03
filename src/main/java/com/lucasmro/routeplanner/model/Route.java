package com.lucasmro.routeplanner.model;

public class Route {
	private String origin;
	private String destination;
	private double distance;

	public Route() {
	}

	public Route(String origin, String destination, double distance) {
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Route [destination=" + origin + ", destination=" + destination
				+ ", distance=" + distance + "]";
	}
}
