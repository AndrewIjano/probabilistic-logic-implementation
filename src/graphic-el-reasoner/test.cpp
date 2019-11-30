#include <iostream>
#include <vector>
#include <set>

#include "read_ontology.cpp"
#include "weighted_graph.h"
#include "max_sat.cpp"

using namespace std;

int main() {
    string ont_str = ReadOntology("example7.owl");

    GELGraph G = GELGraph(ont_str);

    // cout << G.iri_list[0] << endl;
    // cout << G.adj[0][0].vertex << " " << G.adj[0][0].is_derivated
    //      << endl;
    int i = 0;
    for (auto adj : G.adj) {
        for (auto v : adj) {
            cout << i << " " << v.vertex << endl;
            cout << G.iri_list[i] << " " << G.iri_list[v.vertex] << endl;
        }
        i++;
    }
    vector<int> weights = {-1, 1, 1};
    G.adj[7][1].p_axiom_index = 0;
    G.adj[3][0].p_axiom_index = 1;
    G.adj[9][0].p_axiom_index = 2;

    WeightedGraph wG = WeightedGraph(G, weights);
    set<WeightedArrow> cut_set = minCut(wG, 0, 1);
    set<WeightedArrow> arrow_set = wG.arrowSet();
    set<WeightedArrow> result;

    set_difference(arrow_set.begin(), arrow_set.end(), cut_set.begin(),
                   cut_set.end(), inserter(result, result.end()));

    for (auto a : result)
        cout << a.vertex1 << " - (" << a.weight << ") -> " << a.vertex2
             << endl;
             
    return EXIT_SUCCESS;
}