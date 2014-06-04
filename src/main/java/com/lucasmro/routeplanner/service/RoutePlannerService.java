package com.lucasmro.routeplanner.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.Relationship;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;

import com.google.gson.Gson;
import com.lucasmro.routeplanner.enums.RelTypes;
import com.lucasmro.routeplanner.model.Map;
import com.lucasmro.routeplanner.model.Route;
import com.lucasmro.routeplanner.model.RouteDirections;
import com.lucasmro.routeplanner.model.WeightedPath;

public class RoutePlannerService {
	private static String URI = "http://localhost:7474/db/data/";
	private static String NAME = "name";
	private static String DISTANCE = "distance";
	
	private RestAPI api;
	
	public RoutePlannerService() {
		api = new RestAPIFacade(URI);
	}

	public void persist(Map map) {
		for (String nodeName : createDistinctNodes(map)) {
			createNode(nodeName);
		}

		for (Route route : map.getRoutes()) {
			createRelationship(route.getOrigin(), route.getDestination(), route.getDistance());
		}
	}
	
	public RouteDirections getDirections(String origin, String destination, double fuelEfficiency, double fuelPrice) {
		RestNode originNode = findNode(origin);
		RestNode destinationNode = findNode(destination);
		
		if (null == originNode) {
			throwException(404, "Origin not found");
		}
		
		if (null == destinationNode) {
			throwException(404, "Destination not found");
		}
		
		DijkstraService dijkstra = new DijkstraService();
		WeightedPath weightedPath = dijkstra.findSinglePath(originNode, destinationNode);
		RouteDirections routeDirections = createRouteDirections(weightedPath, fuelEfficiency, fuelPrice);
		
		return routeDirections;
	}
	
	private Set<String> createDistinctNodes(Map map) {
		Set<String> distinctNodes = new HashSet<String>();
		
		for (Route route : map.getRoutes()) {
			distinctNodes.add(route.getOrigin());
			distinctNodes.add(route.getDestination());
		}
		
		return distinctNodes;
	}
	
	private RestNode createNode(String name) {
		java.util.Map<String, Object> props = new HashMap<String, Object>();
		props.put(NAME, name);
		
		return api.createNode(props);
	}
	
	private void createRelationship(String origin, String destination, double distance) {
		RestNode originNode = findOrCreateNode(origin);
		RestNode destinationNode = findOrCreateNode(destination);
		
		java.util.Map<String, Object> props = new HashMap<String, Object>();
		props.put(DISTANCE, distance);
		
		Relationship r = originNode.createRelationshipTo(destinationNode, RelTypes.LEADS_TO);
		r.setProperty(DISTANCE, distance);
	}
	
	private RestNode findOrCreateNode(String name) {
		RestNode node = findNode(name);
        
        if (node == null) {
            node = createNode(name);
        }
        
        return node;
	}
	
	private RestNode findNode(String name) {
		String query = String.format("MATCH (node {name: '%s'}) RETURN node", name);
		
		QueryEngine engine = new RestCypherQueryEngine(api);
		QueryResult<java.util.Map<String,Object>> result = engine.query(query, Collections.EMPTY_MAP);
		
		Iterator<java.util.Map<String, Object>> iterator = result.iterator();
		if (iterator.hasNext()) {  
			java.util.Map<String,Object> row = iterator.next();
			return (RestNode) row.get("node");
		}
		
		return null;
	}
	
	private RouteDirections createRouteDirections(WeightedPath weightedPath, double fuelEfficiency, double fuelPrice) {
		double distance = weightedPath.getWeight();
		double cost = calculateCost(fuelEfficiency, fuelPrice, distance);
		
		List<String> directions = new ArrayList<String>();
		
		for (Long nodeId : weightedPath.getPath()) {
			RestNode node = api.getNodeById(nodeId);
			directions.add(node.getProperty(NAME).toString());
		}
		
		RouteDirections routeDirections = new RouteDirections(directions, cost, distance);
		
		return routeDirections;
	}
	
	private double calculateCost(double fuelEfficiency, double fuelPrice, double distance) {
		return (fuelPrice * distance) / fuelEfficiency;
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
