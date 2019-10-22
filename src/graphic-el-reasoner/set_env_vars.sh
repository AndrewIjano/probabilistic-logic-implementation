LIBPATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server/
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${LIBPATH}
export CLASSPATH=$(cat classpath.txt)
