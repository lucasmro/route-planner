package com.lucasmro.routeplanner.controller;

import static com.jayway.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.lucasmro.routeplanner.model.Map;
import com.lucasmro.routeplanner.model.Route;

public class RoutePlannerControllerTest {
	@Test
	public void testPostMapShouldReturnUnsupportedMediaTypeWhenPostEmptyPayload() {
		given().
			contentType("application/json").
		expect().
			statusCode(415).
		when().
			post("/route-planner/route/map");
	}
	
	@Test
	public void testPostMapShouldReturnCreatedWhenPostValidJson() {
		List<Route> routes = new ArrayList<Route>();
		routes.add(new Route("A", "B", 10));
		routes.add(new Route("B", "C", 20));
		routes.add(new Route("C", "D", 15));
		routes.add(new Route("A", "D", 50));
		
		Map map = new Map("SP", routes);
		
		Gson gson = new Gson(); 
	    String json = gson.toJson(map);
	    
		given().
			contentType("application/json").
			body(json).
		expect().
			statusCode(201).
		when().
			post("/route-planner/route/map");

		// TODO: Cleanup data
	}

	@Test
	public void testGetShortestRouteShouldReturnBadRequestWhenMandatoryParametersAreBlank() {
		given().
			contentType("application/json").
		expect().
			statusCode(400).
		when().
			get("/route-planner/route/shortest");
	}

	@Test
	public void testGetShortestRouteShouldReturnSuccessWhenUseValidParameters() {
		// TODO: Add fixtures

		given().
			param("origin", "A").
	        param("destination", "B").
	        param("fuelEfficiency", "8.5").
	        param("fuelPrice", "2.5").
		expect().
			statusCode(200).
		when().
			get("/route-planner/route/shortest");

		// TODO: Cleanup fixtures
	}
}