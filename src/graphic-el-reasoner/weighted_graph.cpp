#include "weighted_graph.h"

using namespace std;

WeightedArrow::WeightedArrow(int v1, int v2, int w) {
    vertex1 = v1;
    vertex2 = v2;
    weight = w;
}

bool operator<(WeightedArrow a1, WeightedArrow a2) {
    if (a1.vertex1 < a2.vertex1) return true;
    if (a1.vertex1 > a2.vertex1) return false;

    if (a1.vertex2 < a2.vertex2) return true;
    if (a1.vertex2 > a2.vertex2) return false;

    if (a1.weight < a2.weight) return true;
    if (a1.weight > a2.weight) return false;

    return false;
}

WeightedGraph::WeightedGraph(GELGraph G) {
    for (auto arrows : G.adj) {
        vector<Node> adj_arrows;
        for (auto arrow : arrows) {
            Node a;
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
        vector<Node> adj_arrows;
        adj.push_back(adj_arrows);
    }
}

void WeightedGraph::addArrow(int vertex1, int vertex2, int weight) {
    if (weight < 0) {
        negative_weight_arrows.insert(
            WeightedArrow(vertex1, vertex2, weight));
        return;
    }

    for (int i = 0; i < adj.at(vertex1).size(); i++) {
        if (adj.at(vertex1).at(i).vertex == vertex2) {
            adj.at(vertex1).at(i).weight = weight;
            return;
        }
    }

    Node a;
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

set<WeightedArrow> WeightedGraph::arrowSet() {
    set<WeightedArrow> edge_set;
    for (int v = 0; v < adj.size(); v++)
        for (auto a : adj.at(v)) {
            WeightedArrow arrow = WeightedArrow(v, a.vertex, a.weight);
            edge_set.insert(arrow);
        }
    return edge_set;
}