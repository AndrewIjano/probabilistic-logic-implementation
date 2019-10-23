#include <iostream>
#include <vector>
#include "nlohmann/json.hpp"

#ifndef GELGRAPH_H
#define GELGRAPH_H

class GELGraph {
   private:
    struct Arrow {
        int role;
        int vertex;
    };

   public:
    std::vector<std::vector<Arrow>> adj;
    std::vector<std::string> iri_list;
    std::vector<std::string> role_list;
    GELGraph(std::string ont_str);
};

#endif