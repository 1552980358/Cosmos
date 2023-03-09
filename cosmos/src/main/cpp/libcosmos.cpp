#include <jni.h>
#include <android/bitmap.h>
#include <iostream>

#include "lib_cosmos_util.h"

#include "cosmos.h"

extern "C"
JNIEXPORT jboolean JNICALL
Java_projekt_cloud_piece_cosmos_LibCosmos_putByteArray(JNIEnv *env, jobject thiz,
                                                       jbyteArray byte_array) {
    auto size = env->GetArrayLength(byte_array);
    if (!size) {
        return false;
    }

    auto byte_array_buffer = (byte_t *) malloc(sizeof(byte_t) * size);
    if (!byte_array_buffer) {
        return false;
    }

    env->GetByteArrayRegion(byte_array, 0, size, byte_array_buffer);

    set_pointer(env,
                thiz,
                get_pinter_field(env, thiz),
                (pointer_t) new cosmos(byte_array_buffer, size)
    );
    return true;
}


extern "C"
JNIEXPORT jbyteArray JNICALL
Java_projekt_cloud_piece_cosmos_LibCosmos_getByteArray(JNIEnv *env, jobject thiz, jlong cosmos_ptr) {
    auto cosmos = (cosmos_t *) cosmos_ptr;
    if (!cosmos_ptr) {
        return nullptr;
    }
    auto byte_array = env->NewByteArray(cosmos->get_size());
    if (!byte_array) {
        return nullptr;
    }
    env->SetByteArrayRegion(byte_array, 0, cosmos->get_size(), cosmos->get_byte_array());
    return byte_array;
}