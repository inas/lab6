//
// Created by Anisha Inas Izdihar on 2019-11-29.
//

#include <jni.h>
#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_inas_anisha_lab6_MainActivity_concatenateString(
        JNIEnv *env,
        jobject, jstring firstString, jstring secondString) {
    char returnString[40];
    const char *fS = env->GetStringUTFChars(firstString, NULL);
    const char *sS = env->GetStringUTFChars(secondString, NULL);

    strcpy(returnString, fS); // copy string one into the result.
    strcat(returnString, sS); // append string two to the result.

    env->ReleaseStringUTFChars(firstString, fS);
    env->ReleaseStringUTFChars(secondString, sS);

    return env->NewStringUTF(returnString);
}