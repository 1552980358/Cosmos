#include <jni.h>
#include <android/bitmap.h>
#include <iostream>

#include "lib_cosmos_util.h"

#include "cosmos.h"

#define jni_void extern "C" JNIEXPORT void JNICALL
#define jni_boolean extern "C" JNIEXPORT jboolean JNICALL
#define jni_byte_array extern "C" JNIEXPORT jbyteArray JNICALL
#define jni_object extern "C" JNIEXPORT jobject JNICALL

jni_boolean
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

jni_byte_array
Java_projekt_cloud_piece_cosmos_LibCosmos_getByteArray(JNIEnv *env, jobject, jlong cosmos_ptr) {
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

jni_void
Java_projekt_cloud_piece_cosmos_LibCosmos_release(JNIEnv *env, jobject thiz, jlong cosmos_ptr) {
    // Delete object instance
    delete (cosmos_t *) cosmos_ptr;
    // Clear pointer value
    env->SetLongField(thiz, get_pinter_field(env, thiz), 0);
}

jni_boolean
Java_projekt_cloud_piece_cosmos_LibCosmos_putBitmap(JNIEnv *env, jobject thiz, jobject jbitmap) {
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

jni_object
Java_projekt_cloud_piece_cosmos_LibCosmos_getBitmap(JNIEnv *env, jobject, jlong pointer) {
    auto cosmos = (cosmos_t *) pointer;
    bitmap_t *bitmap;
    if (!cosmos || !(bitmap = cosmos->get_bitmap())) {
        return nullptr;
    }
    auto bitmap_config = get_jvm_bitmap_config(env, bitmap->format);
    if (!bitmap_config) {
        return nullptr;
    }
    auto jbitmap = create_jvm_bitmap(env, bitmap->width, bitmap->height, bitmap_config);
    if (!jbitmap) {
        return nullptr;
    }

    void *pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, jbitmap, &pixels)) {
        return nullptr;
    }

    // Copy pixels
    memcpy(pixels, cosmos->get_bitmap(), cosmos->get_size());

    // Release
    AndroidBitmap_unlockPixels(env, jbitmap);
    return jbitmap;
}