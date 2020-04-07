//
// Created by IBK on 2020-04-07.
//
#include <jni.h>
#include <string>

extern "C" {

#include "libavformat/avformat.h"

}
extern "C" {
JNIEXPORT jstring  JNICALL
Java_com_example_ptworld_Activity_MainDrawer_stringFromJNI(
        JNIEnv *env,
        jobject){
    std::string hello = "Hello from 이런 쒸삡비비비비";

    av_register_all();

    return env->NewStringUTF(hello.c_str());
}
}
