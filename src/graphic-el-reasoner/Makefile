INC = -I/usr/lib/jvm/java-8-openjdk-amd64/include/ \
-I/usr/lib/jvm/java-8-openjdk-amd64/include/linux

LIB = -L/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server/

test: test.cpp
	g++ -o test -Wall test.cpp gel_graph.cpp $(INC) $(LIB) -ljvm -std=c++11
	