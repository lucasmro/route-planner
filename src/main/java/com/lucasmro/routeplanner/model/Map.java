package com.lucasmro.routeplanner.model;

import java.util.List;

public class Map {
	private String name;
	private List<Route> routes;

	public Map() {
	}

	public Map(String name, List<Route> routes) {
		this.name = name;
		this.routes = routes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	@Override
	public String toString() {
		return "Map [name=" + name + ", routes=" + routes + "]";
	}
}
