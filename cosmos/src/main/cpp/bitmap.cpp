#include "bitmap.h"
#include <string>
using std::string;

typedef uint32_t bitmap_rgba_8888_t;
typedef uint16_t bitmap_rgb_565_t;
typedef uint8_t bitmap_alpha_8_t;

bool check_bitmap_format_support(const int &format) {
    return format == ANDROID_BITMAP_FORMAT_RGBA_8888
           || format == ANDROID_BITMAP_FORMAT_RGB_565
           || format == ANDROID_BITMAP_FORMAT_A_8;
}

bool check_bitmap_format_support(const AndroidBitmapInfo &android_bitmap_info) {
    return check_bitmap_format_support(android_bitmap_info.format);
}

int get_bitmap_format_size(const int &format) {
    switch (format) {
        case ANDROID_BITMAP_FORMAT_RGBA_8888:
            return sizeof(bitmap_rgba_8888_t);
        case ANDROID_BITMAP_FORMAT_RGB_565:
            return sizeof(bitmap_rgb_565_t);
        default:
            return sizeof(bitmap_alpha_8_t);
    }
}

int get_bitmap_format_byte_size(const AndroidBitmapInfo &android_bitmap_info) {
    return get_bitmap_format_size(android_bitmap_info.format) * sizeof(uint8_t); // NOLINT(cppcoreguidelines-narrowing-conversions)
}

string get_jvm_bitmap_config_name(const int32_t &);
jfieldID get_jvm_bitmap_config_field(JNIEnv *, jclass, const string &);

#define BITMAP_CONFIG_CLASS "android/graphics/Bitmap$Config"
jobject get_jvm_bitmap_config(JNIEnv *env, const int32_t &format) {
    if (!check_bitmap_format_support(format)) {
        return nullptr;
    }

    auto bitmap_config_name = get_jvm_bitmap_config_name(format);
    if (bitmap_config_name.empty()) {
        return nullptr;
    }

    auto bitmap_config_class = env->FindClass(BITMAP_CONFIG_CLASS);
    auto bitmap_config_field = get_jvm_bitmap_config_field(env, bitmap_config_class, bitmap_config_name);
    return env->GetStaticObjectField(bitmap_config_class,bitmap_config_field);
}

#define BITMAP_CLASS "android/graphics/Bitmap"
#define BITMAP_CREATE_BITMAP_NAME "createBitmap"
#define BITMAP_CREATE_BITMAP_SIGNATURE "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;"

jobject create_jvm_bitmap(JNIEnv *env, const bitmap_size_t &width, const bitmap_size_t &height, jobject bitmap_config) {
    jclass bitmap = env->FindClass(BITMAP_CLASS);
    jmethodID create_bitmap_method = env->GetStaticMethodID(bitmap,
                                                            BITMAP_CREATE_BITMAP_NAME,
                                                            BITMAP_CREATE_BITMAP_SIGNATURE
                                                            );
    return env->CallStaticObjectMethod(bitmap,
                                       create_bitmap_method,
                                       (jint) width,
                                       (jint) height,
                                       bitmap_config
                                       );
}

#define BITMAP_CONFIG_NOT_SUPPORT ""
#define BITMAP_CONFIG_RGBA_8888 "ARGB_8888"
#define BITMAP_CONFIG_RGB_565 "RGB_565"
#define BITMAP_CONFIG_ALPHA_8 "ALPHA_8"
string get_jvm_bitmap_config_name(const int32_t &format) {
    switch (format) {
        case ANDROID_BITMAP_FORMAT_RGBA_8888:
            return BITMAP_CONFIG_RGBA_8888;
        case ANDROID_BITMAP_FORMAT_RGB_565:
            return BITMAP_CONFIG_RGB_565;
        case ANDROID_BITMAP_FORMAT_A_8:
            return BITMAP_CONFIG_ALPHA_8;
        default:
            return BITMAP_CONFIG_NOT_SUPPORT;
    }
}

#define BITMAP_CONFIG_CLASS_SIGNATURE "Landroid/graphics/Bitmap$Config;"
jfieldID get_jvm_bitmap_config_field(JNIEnv *env, jclass bitmap_config_class, const string &bitmap_config_name) {
    return env->GetStaticFieldID(bitmap_config_class,
                                 bitmap_config_name.c_str(),
                                 BITMAP_CONFIG_CLASS_SIGNATURE
    );
}