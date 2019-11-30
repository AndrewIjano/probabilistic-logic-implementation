#include <jni.h>
#include <iostream>
#include "gel_graph.h"
using namespace std;

string ReadOntology(string ontology) {
    JavaVM *vm;
    JNIEnv *env;

    string classpath_cmd = "-Djava.class.path=";
    string classpath = getenv("CLASSPATH");
    classpath_cmd += classpath;
    JavaVMOption options[1];
    options[0].optionString = (char *)classpath_cmd.c_str();

    JavaVMInitArgs vm_args;
    vm_args.version = JNI_VERSION_1_8;
    vm_args.nOptions = 1;
    vm_args.options = options;
    vm_args.ignoreUnrecognized = 1;

    jint res = JNI_CreateJavaVM(&vm, (void **)&env, &vm_args);

    if (res != JNI_OK) {
        cerr << "ERROR: JNI load failed " << res << endl;
        exit(EXIT_FAILURE);
    }

    cout << "JVM load succeeded: Version ";
    jint ver = env->GetVersion();

    cout << ((ver >> 16) & 0x0f) << "." << (ver & 0x0f) << endl;

    jclass owl_to_gel_class =
        env->FindClass("br/usp/ime/dcc/OWLToGraphicEL");

    if (owl_to_gel_class == nullptr) {
        cerr << "ERROR: class not found !";
        exit(EXIT_FAILURE);
    }

    jmethodID owl_to_json = env->GetStaticMethodID(
        owl_to_gel_class, "OWLToGraphicELJSON",
        "(Ljava/lang/String;)Ljava/lang/String;");

    if (owl_to_json == nullptr) {
        cerr << "ERROR: method not found !" << endl;
        exit(EXIT_FAILURE);
    }

    jstring ont_file = env->NewStringUTF(ontology.c_str());
    jobject result = env->CallStaticObjectMethod(owl_to_gel_class,
                                                 owl_to_json, ont_file);
    const char *str = env->GetStringUTFChars((jstring)result, NULL);

    vm->DestroyJavaVM();

    return string(str);
}

// int main() {
//     string ont_str = ReadOntology("example5.owl");

//     GELGraph G = GELGraph(ont_str);

//     cout << G.iri_list[0] << endl;
//     cout << G.adj[0][0].vertex << " " << G.adj[0][0].is_derivated
//          << endl;
         
//     return EXIT_SUCCESS;
// }