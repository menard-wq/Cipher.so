#include <string>
#include "include/Environments.h"
#include "include/utils.h"
#include "include/extern-keys.h"

using namespace std;

Environments::Environments(JNIEnv *jniEnv, jobject context) {
    this->jniEnv = jniEnv;
    this->context = getApplicationContext(context);
}

bool Environments::check() {
    return checkSignature();
}

bool Environments::checkSignature() {
    string origin_signature(SIGNATURE);
    if (origin_signature.empty()) {
        return true;
    }
    jobject packageManger = getPackageManager();
    jstring packageName = getPackageName();

    jclass coreClz = jniEnv->FindClass("net/idik/lib/cipher/so/CipherCore");
    bool result = false;
    if(coreClz != NULL){
        jmethodID get_sig_method_id = jniEnv->GetStaticMethodID(coreClz, "getSignatures", "(Landroid/content/pm/PackageManager;Ljava/lang/String;)[Landroid/content/pm/Signature;");
        if(get_sig_method_id != NULL){
            jobjectArray signatures = (jobjectArray) jniEnv->CallStaticObjectMethod(coreClz,
                                                                     get_sig_method_id,
                                                                     packageManger,
                                                                     packageName);
            jclass signature_clz = jniEnv->FindClass("android/content/pm/Signature");
            jmethodID get_hashcode_method_id = jniEnv->GetMethodID(signature_clz, "hashCode", "()I");
            int size = jniEnv->GetArrayLength(signatures);
            for (int i = 0; i < size; i++) {
                jobject signature = jniEnv->GetObjectArrayElement(signatures, i);
                int signature_hashcode = jniEnv->CallIntMethod(signature, get_hashcode_method_id);
                jniEnv->DeleteLocalRef(signature);
                if (to_string(signature_hashcode) == origin_signature) {
                    result = true;
                    break;
                }
            }
            jniEnv->DeleteLocalRef(signatures);
            jniEnv->DeleteLocalRef(signature_clz);
        }
    }
    jniEnv->DeleteLocalRef(packageManger);
    jniEnv->DeleteLocalRef(coreClz);
    return result;
}

jobject Environments::getPackageManager() {
    jclass context_clz = jniEnv->GetObjectClass(context);
    jmethodID get_package_manager_method_id = jniEnv->GetMethodID(context_clz,
                                                                  "getPackageManager",
                                                                  "()Landroid/content/pm/PackageManager;");
    jobject package_manager = jniEnv->CallObjectMethod(context, get_package_manager_method_id);
    jniEnv->DeleteLocalRef(context_clz);
    return package_manager;
}

jstring Environments::getPackageName() {
    jclass context_clz = jniEnv->GetObjectClass(context);
    jmethodID get_package_name_method_id = jniEnv->GetMethodID(context_clz,
                                                               "getPackageName",
                                                               "()Ljava/lang/String;");
    jstring packageName = (jstring) jniEnv->CallObjectMethod(context, get_package_name_method_id);
    jniEnv->DeleteLocalRef(context_clz);
    return packageName;
}

jobject Environments::getContext() {
    return context;
}

jobject Environments::getApplicationContext(jobject context) {
    jobject application = NULL;
    jclass application_clz = jniEnv->FindClass("android/app/ActivityThread");
    if (application_clz != NULL) {
        jmethodID current_application_method_id = jniEnv->GetStaticMethodID(application_clz,
                                                                            "currentApplication",
                                                                            "()Landroid/app/Application;");
        if (current_application_method_id != NULL) {
            application = jniEnv->CallStaticObjectMethod(application_clz,
                                                         current_application_method_id);
        }
        jniEnv->DeleteLocalRef(application_clz);
    }
    if (application == NULL) {
        ERROR("ClassNotFoundException: android.app.ActivityThread.class");
        application = context;
    }
    return application;
}