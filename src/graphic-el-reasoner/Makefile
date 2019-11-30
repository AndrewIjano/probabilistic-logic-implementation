INC = -I/usr/lib/jvm/java-8-openjdk-amd64/include/ \
-I/usr/lib/jvm/java-8-openjdk-amd64/include/linux

LIB = -L/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server/

SRCS = read_ontology.cpp gel_graph.cpp weighted_graph.cpp max_sat.cpp

DEPS = gel_graph.cpp weighted_graph.cpp
OBJS = $(DEPS:.cpp=.o)

FLAGS = -Wall

test: max_sat.o read_ontology.o $(OBJS)
	@echo Making test
	g++ $(FLAGS) $(INC) -o test test.cpp $(OBJS) $(LIB) -ljvm -std=c++11

read_ontology: read_ontology.cpp $(OBJS)
	@echo Making read_ontology
	g++ $(FLAGS) $(INC) -o read_ontology read_ontology.cpp $(OBJS) $(LIB) -ljvm -std=c++11


read_ontology.o: read_ontology.cpp $(OBJS)
	@echo Making read_ontology
	g++ $(FLAGS) $(INC) -c $< -o $@ $(OBJS) $(LIB) -ljvm -std=c++11


max_sat: max_sat.o $(OBJS)
	g++ $(FLAGS) -o max_sat max_sat.cpp $(OBJS) -std=c++11

.cpp.o:
	@echo Making $<
	g++ $(FLAGS) -c $< -o $@ -std=c++11

clean:
	rm *.o
	rm read_ontology
	rm max_sat
	rm test