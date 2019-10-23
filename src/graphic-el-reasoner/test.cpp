#include <jni.h>
#include <iostream>
#include "nlohmann/json.hpp"

using namespace std;
using json = nlohmann::json;

int main() {
    JavaVM *vm;
    JNIEnv *env;
    JavaVMInitArgs vm_args;
    vm_args.version = JNI_VERSION_1_8;
    vm_args.nOptions = 1;

    string classpath_cmd = "-Djava.class.path=";
    string classpath = getenv("CLASSPATH");
    classpath_cmd += classpath;
    JavaVMOption options[1];
    options[0].optionString = (char *)classpath_cmd.c_str();
    
    vm_args.options = options;
    vm_args.ignoreUnrecognized = 1;

    jint res = JNI_CreateJavaVM(&vm, (void **)&env, &vm_args);
    
    if (res != JNI_OK) {
        cerr << "ERROR: JNI load failed " << res << endl;
        exit(EXIT_FAILURE);
    }

    cout << "JVM load succeeded: Version ";
    jint ver = env->GetVersion();
    
    cout << ((ver>>16)&0x0f) << "."<<(ver&0x0f) << endl;

    jclass clazz = env->FindClass("br/usp/ime/dcc/Test");

    if (clazz == nullptr) {
        cerr << "ERROR: class not found !";
        return EXIT_FAILURE;
    }
    
    jmethodID test_m =
        env->GetStaticMethodID(clazz, "exampleToGraphicEL", "()Ljava/lang/String;");

    if (test_m == nullptr) {
        cerr << "ERROR: method not found !" << endl;
        return EXIT_FAILURE;
    }

    jobject result = env->CallStaticObjectMethod(clazz, test_m);

    const char *str = env->GetStringUTFChars((jstring)result, NULL);

    cout << str << "\n";

    cout << "Oi!!\n";
    vm->DestroyJavaVM();
    return EXIT_SUCCESS;
}