#ifndef COSMOS_LIB_COSMOS_UTIL_H
#define COSMOS_LIB_COSMOS_UTIL_H

#include <jni.h>
#include "cosmos.h"

typedef jobject lib_cosmos_t;
typedef long long pointer_t;

jfieldID get_pinter_field(JNIEnv *, lib_cosmos_t);

cosmos_t *get_pointer(JNIEnv *, lib_cosmos_t, jfieldID);
void set_pointer(JNIEnv *, lib_cosmos_t, jfieldID, pointer_t);

#endif //COSMOS_LIB_COSMOS_UTIL_H
