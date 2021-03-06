/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_github_morgaroth_eventsmanger_EventDevice */

#ifndef _Included_io_github_morgaroth_eventsmanger_EventDevice
#define _Included_io_github_morgaroth_eventsmanger_EventDevice
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_github_morgaroth_eventsmanger_EventDevice
 * Method:    ioctlGetID
 * Signature: (Ljava/lang/String;[S)Z
 */
JNIEXPORT jboolean JNICALL Java_io_github_morgaroth_eventsmanger_EventDevice_ioctlGetID
  (JNIEnv *, jobject, jstring, jshortArray);

/*
 * Class:     io_github_morgaroth_eventsmanger_EventDevice
 * Method:    ioctlGetEvdevVersion
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_io_github_morgaroth_eventsmanger_EventDevice_ioctlGetEvdevVersion
  (JNIEnv *, jobject, jstring);

/*
 * Class:     io_github_morgaroth_eventsmanger_EventDevice
 * Method:    ioctlGetDeviceName
 * Signature: (Ljava/lang/String;[B)Z
 */
JNIEXPORT jboolean JNICALL Java_io_github_morgaroth_eventsmanger_EventDevice_ioctlGetDeviceName
  (JNIEnv *, jobject, jstring, jbyteArray);

/*
 * Class:     io_github_morgaroth_eventsmanger_EventDevice
 * Method:    ioctlEVIOCGBIT
 * Signature: (Ljava/lang/String;[JII)Z
 */
JNIEXPORT jboolean JNICALL Java_io_github_morgaroth_eventsmanger_EventDevice_ioctlEVIOCGBIT
  (JNIEnv *, jobject, jstring, jlongArray, jint, jint);

/*
 * Class:     io_github_morgaroth_eventsmanger_EventDevice
 * Method:    ioctlEVIOCGABS
 * Signature: (Ljava/lang/String;[II)Z
 */
JNIEXPORT jboolean JNICALL Java_io_github_morgaroth_eventsmanger_EventDevice_ioctlEVIOCGABS
  (JNIEnv *, jobject, jstring, jintArray, jint);

#ifdef __cplusplus
}
#endif
#endif
