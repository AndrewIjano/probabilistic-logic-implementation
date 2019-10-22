#include <jni.h>
#include <iostream>

using namespace std;

int main() {
    JavaVM *vm;
    JNIEnv *env;
    JavaVMInitArgs vm_args;
    vm_args.version = JNI_VERSION_1_8;
    vm_args.nOptions = 2;
    JavaVMOption options[2];
    options[0].optionString = "-Djava.class.path=/home/andrew/Documents/USP/IC/logics/probabilistic-logic-implementation/src/owl-to-graphic-el/target/classes:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-distribution/5.0.0/owlapi-distribution-5.0.0.jar:/home/andrew/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.6.3/jackson-core-2.6.3.jar:/home/andrew/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.6.3/jackson-databind-2.6.3.jar:/home/andrew/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.6.3/jackson-annotations-2.6.3.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-compatibility/5.0.0/owlapi-compatibility-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-apibinding/5.0.0/owlapi-apibinding-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-api/5.0.0/owlapi-api-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-impl/5.0.0/owlapi-impl-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-parsers/5.0.0/owlapi-parsers-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-oboformat/5.0.0/owlapi-oboformat-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-tools/5.0.0/owlapi-tools-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-fixers/5.0.0/owlapi-fixers-5.0.0.jar:/home/andrew/.m2/repository/net/sourceforge/owlapi/owlapi-rio/5.0.0/owlapi-rio-5.0.0.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-jsonld/4.0.2/sesame-rio-jsonld-4.0.2.jar:/home/andrew/.m2/repository/org/apache/commons/commons-rdf-api/0.1.0-incubating/commons-rdf-api-0.1.0-incubating.jar:/home/andrew/.m2/repository/org/tukaani/xz/1.5/xz-1.5.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-model/4.0.2/sesame-model-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-util/4.0.2/sesame-util-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-api/4.0.2/sesame-rio-api-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-languages/4.0.2/sesame-rio-languages-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-datatypes/4.0.2/sesame-rio-datatypes-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-binary/4.0.2/sesame-rio-binary-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-n3/4.0.2/sesame-rio-n3-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-nquads/4.0.2/sesame-rio-nquads-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-ntriples/4.0.2/sesame-rio-ntriples-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-rdfjson/4.0.2/sesame-rio-rdfjson-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-rdfxml/4.0.2/sesame-rio-rdfxml-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-trix/4.0.2/sesame-rio-trix-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-turtle/4.0.2/sesame-rio-turtle-4.0.2.jar:/home/andrew/.m2/repository/org/openrdf/sesame/sesame-rio-trig/4.0.2/sesame-rio-trig-4.0.2.jar:/home/andrew/.m2/repository/com/github/jsonld-java/jsonld-java/0.8.0/jsonld-java-0.8.0.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/httpclient-osgi/4.5.1/httpclient-osgi-4.5.1.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/httpclient/4.5.1/httpclient-4.5.1.jar:/home/andrew/.m2/repository/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/httpmime/4.5.1/httpmime-4.5.1.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/httpclient-cache/4.5.1/httpclient-cache-4.5.1.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/fluent-hc/4.5.1/fluent-hc-4.5.1.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/httpcore-osgi/4.4.4/httpcore-osgi-4.4.4.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/httpcore/4.4.4/httpcore-4.4.4.jar:/home/andrew/.m2/repository/org/apache/httpcomponents/httpcore-nio/4.4.4/httpcore-nio-4.4.4.jar:/home/andrew/.m2/repository/org/slf4j/jcl-over-slf4j/1.7.13/jcl-over-slf4j-1.7.13.jar:/home/andrew/.m2/repository/org/semarglproject/semargl-sesame/0.6.1/semargl-sesame-0.6.1.jar:/home/andrew/.m2/repository/org/semarglproject/semargl-core/0.6.1/semargl-core-0.6.1.jar:/home/andrew/.m2/repository/org/semarglproject/semargl-rdfa/0.6.1/semargl-rdfa-0.6.1.jar:/home/andrew/.m2/repository/org/semarglproject/semargl-rdf/0.6.1/semargl-rdf-0.6.1.jar:/home/andrew/.m2/repository/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:/home/andrew/.m2/repository/com/github/ben-manes/caffeine/caffeine/2.1.0/caffeine-2.1.0.jar:/home/andrew/.m2/repository/com/google/guava/guava/19.0/guava-19.0.jar:/home/andrew/.m2/repository/com/google/inject/guice/4.0/guice-4.0.jar:/home/andrew/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/home/andrew/.m2/repository/aopalliance/aopalliance/1.0/aopalliance-1.0.jar:/home/andrew/.m2/repository/com/google/inject/extensions/guice-assistedinject/4.0/guice-assistedinject-4.0.jar:/home/andrew/.m2/repository/com/google/inject/extensions/guice-multibindings/4.0/guice-multibindings-4.0.jar:/home/andrew/.m2/repository/com/google/code/findbugs/jsr305/2.0.1/jsr305-2.0.1.jar:/home/andrew/.m2/repository/org/slf4j/slf4j-api/1.7.14/slf4j-api-1.7.14.jar:/home/andrew/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar";
    options[1].optionString = "-verbose:jni";
    
    vm_args.options = options;
    vm_args.ignoreUnrecognized = 1;

    jint res = JNI_CreateJavaVM(&vm, (void **)&env, &vm_args);
    
    if (res != JNI_OK) {
        cerr << "FAILED: JNI_CreateJavaVM " << res << endl;
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

    // const char *str = env->GetStringUTFChars((jstring)result, NULL);

    // cout << str << "\n";
    // env->ReleaseStringUTFChars(jstr, str);

    cout << "Oi!!\n";
    vm->DestroyJavaVM();
    return EXIT_SUCCESS;
}