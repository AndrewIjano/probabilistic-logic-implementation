#include "gel_graph.h"

using namespace std;
using json = nlohmann::json;

GELGraph::GELGraph(string ont_str) {
    json j = json::parse(ont_str);

    for (string iri : j["vertices"]) {
        iri_list.push_back(iri);
    }

    for (string role : j["roles"]) {
        role_list.push_back(role);
    }

    for (auto arrows : j["arrows"]) {
        vector<Arrow> adj_arrows;
        for (auto arrow : arrows) {
            Arrow a;
            a.role = arrow["role"];
            a.vertex = arrow["vertex"];
            adj_arrows.push_back(a);
        }
        adj.push_back(adj_arrows);
    }
}