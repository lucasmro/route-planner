package com.lucasmro.routeplanner.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lucasmro.routeplanner.enums.RelTypes;
import com.lucasmro.routeplanner.model.WeightedPath;

public class DijkstraService {
	private static String WEIGHT = "weight";
	private static String NODES = "nodes";
	private static String TYPE = "type";
	private static String DIRECTION = "direction";
	private static String OUT = "out";
	private static String TO = "to";
	private static String COST_PROPERTY = "cost_property";
	private static String DISTANCE = "distance";
	private static String RELATIONSHIPS = "relationships";
	private static String ALGORITHM = "algorithm";
	private static String DIJKSTRA = "dijkstra";
	
	private final Logger log = LoggerFactory.getLogger(DijkstraService.class);
	
	public DijkstraService() {
	}

	public WeightedPath findSinglePath(RestNode originNode, RestNode destinationNode) {
		JsonObject jsonObject = doPost(originNode, destinationNode);
		
		// Weight
		JsonElement weightElement = jsonObject.get(WEIGHT);
		double weight = weightElement.getAsDouble();
		
		// Path
		JsonElement nodes = jsonObject.get(NODES);
		JsonArray jsonArray = nodes.getAsJsonArray();
		List<Long> path = new ArrayList<Long>();
		
		Iterator<JsonElement> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			JsonElement jsonElement = iterator.next();
			String nodeURI = jsonElement.getAsString();
			
			int lastIndex = nodeURI.lastIndexOf('/');
			String nodeId = nodeURI.substring(lastIndex + 1);
			path.add(Long.parseLong(nodeId));
		}
		
		WeightedPath weightedPath = new WeightedPath(weight, path);
		
		return weightedPath;
	}
	
	private JsonObject doPost(RestNode originNode, RestNode destinationNode) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(originNode.getUri() + "/path");
	 
			String payload = createJsonPayload(destinationNode);
			System.out.println(payload);
			
			StringEntity input = new StringEntity(payload);
			input.setContentType(MediaType.APPLICATION_JSON);
			postRequest.setEntity(input);
	 
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();
			
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append (line);
			}
			
			httpClient.getConnectionManager().shutdown();
			
			String jsonResponse = sb.toString();
			JsonParser parser = new JsonParser();
			
			return (JsonObject) parser.parse(jsonResponse);
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return null;
	}
	
	private String createJsonPayload(RestNode destinationNode) {
		JsonObject relationships = new JsonObject();
		relationships.addProperty(TYPE, RelTypes.LEADS_TO.toString());
		relationships.addProperty(DIRECTION, OUT);
		
		JsonObject obj = new JsonObject();
		obj.addProperty(TO, destinationNode.getUri());
		obj.addProperty(COST_PROPERTY, DISTANCE);
		obj.add(RELATIONSHIPS, relationships);
		obj.addProperty(ALGORITHM, DIJKSTRA);
		
		return obj.toString();
	}
}
