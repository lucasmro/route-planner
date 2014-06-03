# Route Planner

Encontrar o caminho mais rápido entre dois pontos.

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
