package com.lucasmro.routeplanner.model;

import java.util.List;

public class WeightedPath {
	private double weight;
	private List<Long> path;
	
	public WeightedPath() {
	}

	public WeightedPath(double weight, List<Long> path) {
		this.weight = weight;
		this.path = path;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<Long> getPath() {
		return path;
	}

	public void setPath(List<Long> path) {
		this.path = path;
	}
}
