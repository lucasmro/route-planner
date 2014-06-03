package com.lucasmro.routeplanner.model;

import java.util.List;

public class RouteDirections {
	private List<String> directions;
	private double cost;
	private double distance;

	public RouteDirections() {
	}

	public RouteDirections(List<String> directions, double cost, double distance) {
		this.directions = directions;
		this.cost = cost;
		this.distance = distance;
	}

	public List<String> getDirections() {
		return directions;
	}

	public void setDirections(List<String> directions) {
		this.directions = directions;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
