INC = -I/usr/lib/jvm/java-8-openjdk-amd64/include/ \
-I/usr/lib/jvm/java-8-openjdk-amd64/include/linux

LIB = -L/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server/

SRCS = read_ontology.cpp gel_graph.cpp weighted_graph.cpp min_cut.cpp

DEPS = gel_graph.cpp weighted_graph.cpp
OBJS = $(DEPS:.cpp=.o)

FLAGS = -Wall

read_ontology: read_ontology.cpp $(OBJS)
	@echo Making read_ontology
	g++ $(FLAGS) $(INC) -o read_ontology read_ontology.cpp $(OBJS) $(LIB) -ljvm -std=c++11

maxsat: min_cut.o $(OBJS)
	g++ $(FLAGS) -o min_cut min_cut.cpp $(OBJS) -std=c++11

.cpp.o:
	@echo Making $<
	g++ $(FLAGS) -c $< -o $@ -std=c++11

clean:
	rm *.o
	rm read_ontology