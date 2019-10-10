#include <jni.h>
#include <iostream>

using namespace std;

int main() {
    JavaVM *vm;
    JNIEnv *env;
    JavaVMInitArgs vm_args;
    vm_args.version = JNI_VERSION_1_2;
    vm_args.nOptions = 0;
    vm_args.ignoreUnrecognized = 1;

    jint res = JNI_CreateJavaVM(&vm, (void **)&env, &vm_args);

    jstring jstr = env->NewStringUTF("Hello World!");

    jclass clazz = env->FindClass("java/lang/String");

    jmethodID to_lower =
        env->GetMethodID(clazz, "toLowerCase", "()Ljava/lang/String;");

    jobject result = env->CallObjectMethod(jstr, to_lower);

    const char *str = env->GetStringUTFChars((jstring)result, NULL);

    cout << str << "\n";

    env->ReleaseStringUTFChars(jstr, str);
    vm->DestroyJavaVM();
    return 0;
}