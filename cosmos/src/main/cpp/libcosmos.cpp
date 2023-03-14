#include <jni.h>
#include <android/bitmap.h>
#include <iostream>

#include "lib_cosmos_util.h"

#include "cosmos.h"


#define jni_boolean extern "C" JNIEXPORT jboolean JNICALL

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

extern "C"
JNIEXPORT void JNICALL
Java_projekt_cloud_piece_cosmos_LibCosmos_release(JNIEnv *env, jobject thiz, jlong cosmos_ptr) {
    // Delete object instance
    delete (cosmos_t *) cosmos_ptr;
    // Clear pointer value
    env->SetLongField(thiz, get_pinter_field(env, thiz), 0);
}

#define cosmos_put_bitmap Java_projekt_cloud_piece_cosmos_LibCosmos_putBitmap

jni_boolean cosmos_put_bitmap(JNIEnv *env, jobject thiz, jobject jbitmap) {
    AndroidBitmapInfo android_bitmap_info;
    if (AndroidBitmap_getInfo(env, jbitmap, &android_bitmap_info)/** != ANDROID_BITMAP_RESULT_SUCCESS **/
        || !check_bitmap_format_support(android_bitmap_info)) {
        return false;
    }

    auto pixel_size = android_bitmap_info.width * android_bitmap_info.height *
            get_bitmap_format_byte_size(android_bitmap_info);

    auto byte_array = (byte *) malloc(pixel_size);
    if (!byte_array) {
        return false;
    }

    void *locked_pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, jbitmap, &locked_pixels)/** != ANDROID_BITMAP_RESULT_SUCCESS **/) {
        free(byte_array);
        return false;
    }

    memcpy(locked_pixels, byte_array, pixel_size);
    AndroidBitmap_unlockPixels(env, jbitmap);

    auto bitmap = new bitmap_t { android_bitmap_info.width, android_bitmap_info.height, android_bitmap_info.format };

    set_pointer(env,
                thiz,
                get_pinter_field(env, thiz),
                (pointer_t) new cosmos(byte_array, (array_size_t) pixel_size, bitmap)
    );
    return true;
}