#ifndef CIPHERSO_ENVIRONMENTCHECKER_H
#define CIPHERSO_ENVIRONMENTCHECKER_H

#include <jni.h>

class Environments {

private:

    JNIEnv *jniEnv;
    jobject context;

    jobject getPackageManager();

    jstring getPackageName();

    bool checkSignature();

public:
    Environments(JNIEnv *env, jobject context);

    bool check();

    jobject getContext();

    jobject getApplicationContext(jobject context);
};


#endif //CIPHERSO_ENVIRONMENTCHECKER_H
