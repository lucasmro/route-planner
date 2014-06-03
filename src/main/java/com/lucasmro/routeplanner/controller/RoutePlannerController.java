package com.lucasmro.routeplanner.controller;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.lucasmro.routeplanner.model.Map;

@Path("/route")
public class RoutePlannerController {
	@GET
	@Path("/shortest")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getShortestRoute(@QueryParam("origin") String origin, @QueryParam("destination") String destination, @QueryParam("fuelEfficiency") Double fuelEfficiency, @QueryParam("fuelPrice") Double fuelPrice) {
		validateShortestRouteParameters(origin, destination, fuelEfficiency, fuelPrice);
		
		return Response.status(200).build();
	}
	
	@POST
	@Path("/map")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveMap(Map map) {
		return Response.status(Response.Status.CREATED).build();
	}
	
	private void validateShortestRouteParameters(String origin, String destination, Double fuelEfficiency, Double fuelPrice) {
		if (origin == null) {
			throwException(400, "Parameter origin is mandatory");
		}
		
		if (destination == null) {
			throwException(400, "Parameter destination is mandatory");
		}

		if (fuelEfficiency == null) {
			throwException(400, "Parameter fuelEfficiency is mandatory");
		}

		if (fuelPrice == null) {
			throwException(400, "Parameter fuelPrice is mandatory");
		}
	}
	
	private void throwException(int statusCode, String message) {
		java.util.Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", statusCode);
		result.put("message", message);
				
		Gson gson = new Gson();
	    String json = gson.toJson(result);
	    
		Response response = Response.status(statusCode)
				.entity(json)
				.build();
		
		throw new WebApplicationException(response);
	}
	
}
