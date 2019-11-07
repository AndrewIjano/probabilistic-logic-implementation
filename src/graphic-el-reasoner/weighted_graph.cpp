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
                a.weight = INF;
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

void WeightedGraph::addArrow(int vertex1, int vertex2, int weight) {
    if (weight < 0) {
        WeightedGraph::addArrow(vertex2, vertex1, weight * (-1));
        return;
    }
    
    for (int i = 0; i < adj.at(vertex1).size(); i++) {
        if (adj.at(vertex1).at(i).vertex == vertex2) {
            adj.at(vertex1).at(i).weight = weight;
            return;
        }
    }
    
    Arrow a;
    a.vertex = vertex2;
    a.weight = weight;
    adj.at(vertex1).push_back(a);
}

int WeightedGraph::getWeight(int vertex1, int vertex2) {
    for (auto a : adj.at(vertex1))
        if (a.vertex == vertex2) return a.weight;
    return 0;
}

int WeightedGraph::order() { return adj.size(); }