#include <iostream>
#include <utility>
#include <vector>
#include "nlohmann/json.hpp"

#ifndef GELGRAPH_H
#define GELGRAPH_H

class GELGraph {
   private:
    struct Arrow {
        int role;
        int vertex;
        bool is_derivated;
        int p_axiom_index;
    };

   public:
    std::vector<std::vector<Arrow>> adj;
    std::vector<std::string> iri_list;
    std::vector<std::string> role_list;
    std::vector<std::pair<int, int>> role_inclusions;
    std::vector<std::pair<std::pair<int, int>, int>>
        chained_role_inclusions;
    GELGraph(std::string ont_str);
    int init;
    int bottom;
};

#endif