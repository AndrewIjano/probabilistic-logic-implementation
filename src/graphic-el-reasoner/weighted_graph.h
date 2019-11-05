#include <iostream>
#include <utility>
#include <vector>
#include "gel_graph.h"

#ifndef WEIGHTED_GRAPH_H
#define WEIGHTED_GRAPH_H

#define INFINITY -1
class WeightedGraph {
   private:
    struct Arrow {
        int vertex;
        int weight;
    };

   public:
    std::vector<std::vector<Arrow>> adj;
    std::vector<std::pair<int, int>> role_inclusions;
    std::vector<std::pair<std::pair<int, int>, int>>
        chained_role_inclusions;
    WeightedGraph(GELGraph G);
    WeightedGraph(int V);
};

#endif