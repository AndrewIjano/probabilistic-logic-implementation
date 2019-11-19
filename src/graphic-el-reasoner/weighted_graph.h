#include <climits>
#include <iostream>
#include <utility>
#include <vector>
#include <set>

#include "gel_graph.h"

#ifndef WEIGHTED_GRAPH_H
#define WEIGHTED_GRAPH_H

#define INF INT_MAX

class WeightedArrow {
   public:
    int vertex1;
    int vertex2;
    int weight;

    WeightedArrow(int vertex1, int vertex2, int weight);

    friend bool operator < (WeightedArrow a1, WeightedArrow a2);
};

class WeightedGraph {
   private:
    struct Node {
        int vertex;
        int weight;
    };

   public:
    std::vector<std::vector<Node>> adj;
    std::vector<std::pair<int, int>> role_inclusions;
    std::vector<std::pair<std::pair<int, int>, int>>
        chained_role_inclusions;
    std::set<WeightedArrow> negative_weight_arrows;
    WeightedGraph(GELGraph G);
    WeightedGraph(int V);
    void addArrow(int vertex1, int vertex2, int weight);
    int getWeight(int vertex1, int vertex2);
    int order();
    std::set<WeightedArrow> arrowSet();
};

#endif