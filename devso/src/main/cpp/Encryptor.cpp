#include "include/Encryptor.h"
#include "include/utils.h"

Encryptor::Encryptor(JNIEnv *jniEnv, jobject context) {
    this->jniEnv = jniEnv;
    this->context = context;
}

const char *Encryptor::decrypt(const char *key, const char *iv, const char *cipher_message) {

    jclass encrypt_clz = jniEnv->FindClass("net/idik/lib/cipher/so/encrypt/AESEncryptor");
    if (encrypt_clz != NULL) {
        jmethodID decrypt_method_id = jniEnv->GetStaticMethodID(encrypt_clz, "dec",
                                                                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
        if (decrypt_method_id != NULL) {
            jstring keyString = jniEnv->NewStringUTF(key);
            jstring ivSpec = jniEnv->NewStringUTF(iv);
            jstring cipherString = jniEnv->NewStringUTF(cipher_message);
            jstring result = (jstring) jniEnv->CallStaticObjectMethod(encrypt_clz,
                                                                      decrypt_method_id,
                                                                      keyString,
                                                                      ivSpec,
                                                                      cipherString);
            const char *resultChars = jniEnv->GetStringUTFChars(result, JNI_FALSE);
            jniEnv->DeleteLocalRef(keyString);
            jniEnv->DeleteLocalRef(ivSpec);
            jniEnv->DeleteLocalRef(cipherString);
            jniEnv->DeleteLocalRef(result);
            return resultChars;
        }
    }
    return NULL;
}
