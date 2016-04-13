Route Planner
=======

API para encontrar o caminho mais rápido entre dois pontos utilizando o Neo4j.

## Desafio

Desenvolver um sistema de entregas visando sempre o menor custo.
Para popular sua base de dados o sistema precisa expor um Webservices que aceite o formato de malha logística (exemplo abaixo) e nesta mesma requisição o requisitante deverá informar um nome para este mapa.
É importante que os mapas sejam persistidos para evitar que a cada novo deploy todas as informações desapareçam.
O formato de malha logística é bastante simples, cada linha mostra uma rota: ponto de origem, ponto de destino e distância entre os pontos em quilômetros.

A B 10

B D 15

A C 20

C D 30

B E 50

D E 30

Um exemplo de entrada seria, origem A, destino D, autonomia 10, valor do litro 2,50; a resposta seria a rota A B D com custo de 6,75.

## Motivação

Devido a necessidade de encontrar o menor caminho entre dois pontos, ficou evidente que as relações entre as entidades são mais importante do que o dado em si.
No tradicional modelo de banco de dados relacional, uma consulta para analisar esses relacionamentos seria bastante complexa.

Diante disso, uma possível solução seria trocar o banco de dados para um modelo baseado em grafo.
O banco de dados escolhido para a implementação da solução foi o Neo4j, por ser open source, contar com uma boa documentação, ter uma comunidade ativa e ser a maior referência nesse modelo de banco de dados em grafo.

Além disso, o Neo4j já implementa internamente o algoritmo Dijkstra, que é um algoritmo otimizado para a busca do menor caminho.

## Requisitos

- Java 7
- Tomcat 7
- Neo4j Server 2.0.3 Community Edition

## Configuração

### Neo4j

1 - Faça o [download](http://www.neo4j.org/download) do Neo4j Server 2.0.3 Community Edition

2 - Extraia o conteúdo do arquivo e inicie o servidor

```html
$bin/neo4j start
```

3 - Acesse o painel do servidor no endereço [http://localhost:7474/](http://localhost:7474/)

## Como utilizar

Com o servidor Web e o Neo4j rodando, execute as requisições a seguir para criar o mapa logístico e para encontrar o menor caminho.

### Criar um mapa logístico

**Exemplo de requisição:**

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

**Exemplo de resposta:**

- **201** CREATED

### Encontrar o menor caminho

**Exemplo de requisição:**

- **GET** [http://localhost:8080/route-planner/route/shortest?origin=A&destination=D&fuelEfficiency=10&fuelPrice=2.5](http://localhost:8080/route-planner/route/shortest?origin=A&destination=D&fuelEfficiency=10&fuelPrice=2.5)
- **Accept:** application/json
- **Content-Type:** application/json

**Exemplo de resposta:**

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

## Códigos de erro

| Código | Descrição   | Motivo                                       |
| ------ | ----------- | -------------------------------------------- |
| 400    | Bad Request | Parâmetros ausentes ou parâmetros inválidos. |
| 404    | Not Found   | Valor não encontrado no banco de dados.      |
