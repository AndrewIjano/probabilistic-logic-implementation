#include "weighted_graph.h"

using namespace std;

WeightedGraph::WeightedGraph(GELGraph G) {
    for (auto arrows : G.adj) {
        vector<Arrow> adj_arrows;
        for (auto arrow : arrows) {
            Arrow a;
            a.vertex = arrow.vertex;
            if (arrow.is_derivated)
                a.weight = 0;
            else
                a.weight = INFINITY;
            adj_arrows.push_back(a);
        }
        adj.push_back(adj_arrows);
    }

    role_inclusions = G.role_inclusions;
    chained_role_inclusions = G.chained_role_inclusions;
}

WeightedGraph::WeightedGraph(int V) {
    for (int i = 0; i < V; i++) {
        vector<Arrow> adj_arrows;
        adj.push_back(adj_arrows);
    }
}