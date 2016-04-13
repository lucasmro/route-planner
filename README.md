Route Planner
=======

**To read this document in Brazilian Portuguese, access the [README.pt-br.md](README.pt-br.md) file.**

API to find the fastest route using Neo4j.

## Challenge

Develop a delivery system always prioritizing the lowest cost path.
To populate your database, the system needs to expose Web services that accept the logistics network format (example below) and in this same request the name for this map must be provided.
The persistence of these maps is very important in order to prevent the information from disappearing after deployment.
The logistics network format is quite simple, each line shows a route: origin, destination and distance (in kilometers) between these points.

A B 10

B D 15

A C 20

C D 30

B E 50

D E 30

A sample entry would be, origin A, destination D, autonomy 10, liter value 2.50; the correct answer would be the route A B D with a cost of 6.75.

## Motivation

Due to the need to find the shortest path between two points, it became clear that the relationship between the entities is more important than the data itself.
In a traditional relational database model, a query to analyze these relationships would be quite complex.

Therefore, a possible solution would be to choose a graph-based database.
The database chosen for the implementation of the solution was the Neo4j, because it is open source, relies on good documentation, has an active community and because it is the largest reference in this field.

Furthermore, Neo4j already implements the Dijkstra algorithm, which is an optimal algorithm to search for the shortest path.


## Requirements

- Java 7
- Tomcat 7
- Neo4j Server 2.0.3 Community Edition

## Configuration

### Neo4j

1 - [Download](http://www.neo4j.org/download) the Neo4j Server 2.0.3 Community Edition.

2 - Extract the archive contents and start the server.

```html
$bin/neo4j start
```

3 - Access the server panel at the address [http://localhost:7474/](http://localhost:7474/).

## How to use

With the Web server and the Neo4j running, perform the following requests to create the logistic map and to find the shortest path.

### Create a logistic map

**Example request:**

- **POST** [http://localhost:8080/route-planner/route/map](http://localhost:8080/route-planner/route/map)
- **Accept:** application/json
- **Content-Type:** application/json

```html
	{
		"name": "SP",
		"routes": [
			{
				"distance": 10,
				"origin": "A",
				"destination": "B"
			},
			{
				"distance": 15,
				"origin": "B",
				"destination": "D"
			},
			{
				"distance": 20,
				"origin": "A",
				"destination": "C"
			},
			{
				"distance": 30,
				"origin": "C",
				"destination": "D"
			},
			{
				"distance": 50,
				"origin": "B",
				"destination": "E"
			},
			{
				"distance": 30,
				"origin": "D",
				"destination": "E"
			}
		]
	}
```

**Example response:**

- **201** CREATED

### Find the shortest path

**Example request:**

- **GET** [http://localhost:8080/route-planner/route/shortest?origin=A&destination=D&fuelEfficiency=10&fuelPrice=2.5](http://localhost:8080/route-planner/route/shortest?origin=A&destination=D&fuelEfficiency=10&fuelPrice=2.5)
- **Accept:** application/json
- **Content-Type:** application/json

**Example response:**

- **200** OK

```html
	{
		"distance": 25,
		"cost": 6.25,
		"directions": [
			"A",
			"B",
			"D"
		]
	}
```

## Error Codes

| Code | Description | Reason                                    |
| ---- | ----------- | ----------------------------------------- |
| 400  | Bad Request | Missing parameters or invalid parameters. |
| 404  | Not Found   | Value not found in the database.	         |
