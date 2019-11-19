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
            a.is_derivated = arrow["derivated"];
            adj_arrows.push_back(a);
        }
        adj.push_back(adj_arrows);
    }

    for (auto role_inclusion : j["roleInclusions"]) {
        pair<int, int> ri;
        ri.first = role_inclusion["first"];
        ri.second = role_inclusion["second"];
        role_inclusions.push_back(ri);
    }

    for (auto chain_role_inclusion : j["chainedRoleInclusions"]) {
        pair<int, int> chained_role;
        pair<pair<int, int>, int> cri;
        chained_role.first = chain_role_inclusion["first"];
        chained_role.second = chain_role_inclusion["second"];
        cri.first = chained_role;
        cri.second = chain_role_inclusion["third"];
        chained_role_inclusions.push_back(cri);
    }

    init = 0;
}