#include <limits.h>
#include <string.h>
#include <algorithm>
#include <iostream>
#include <queue>
#include <set>

#include "weighted_graph.h"

using namespace std;

bool bfs(WeightedGraph rG, int s, int t, int parent[]) {
    bool visited[rG.order()];
    memset(visited, false, sizeof(visited));

    queue<int> q;
    q.push(s);
    visited[s] = true;
    parent[s] = -1;

    while (!q.empty()) {
        int u = q.front();
        q.pop();

        for (auto a : rG.adj[u]) {
            int v = a.vertex;
            if (!visited[v] && rG.getWeight(u, v) > 0) {
                q.push(v);
                parent[v] = u;
                visited[v] = true;
            }
        }
    }
    return visited[t];
}

void dfs(WeightedGraph rG, int s, bool visited[]) {
    visited[s] = true;
    for (auto a : rG.adj[s])
        if (!visited[a.vertex] && rG.getWeight(s, a.vertex) > 0)
            dfs(rG, a.vertex, visited);
}

set<WeightedArrow> minCut(WeightedGraph G, int s, int t) {
    WeightedGraph rG = G;
    int parent[rG.order()];

    while (bfs(rG, s, t, parent)) {
        int path_flow = INT_MAX;
        for (int v = t; v != s; v = parent[v]) {
            int u = parent[v];
            path_flow = min(path_flow, rG.getWeight(u, v));
        }

        for (int v = t; v != s; v = parent[v]) {
            int u = parent[v];
            rG.addArrow(u, v, rG.getWeight(u, v) - path_flow);
            rG.addArrow(v, u, rG.getWeight(v, u) + path_flow);
        }
    }

    bool visited[rG.order()];
    memset(visited, false, sizeof(visited));
    dfs(rG, s, visited);

    set<WeightedArrow> cut_set;
    for (int v = 0; v < G.order(); v++)
        for (auto a : G.adj[v])
            if (visited[v] && !visited[a.vertex]) {
                WeightedArrow arrow =
                    WeightedArrow(v, a.vertex, a.weight);
                cut_set.insert(arrow);
            }

    return cut_set;
}

/*
     0
  1 / \ 0
   v  v
   2  3
INF|  | 1
   v  v
   4->1
    2
*/
int main() {
    WeightedGraph G = WeightedGraph(5);

    G.addArrow(0, 2, INF);
    G.addArrow(0, 3, INF);
    G.addArrow(2, 4, 2);
    G.addArrow(3, 1, 1);
    G.addArrow(4, 1, 2);

    set<WeightedArrow> cut_set = minCut(G, 0, 1);
    set<WeightedArrow> arrow_set = G.arrowSet();
    set<WeightedArrow> result;

    set_difference(arrow_set.begin(), arrow_set.end(), cut_set.begin(),
                   cut_set.end(), inserter(result, result.end()));

    for (auto a : result)
        cout << a.vertex1 << " - (" << a.weight << ") -> " << a.vertex2
             << endl;

    return 0;
}
