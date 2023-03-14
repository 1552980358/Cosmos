#include "bitmap.h"

typedef uint32_t bitmap_rgba_8888_t;
typedef uint16_t bitmap_rgb_565_t;
typedef uint8_t bitmap_alpha_8_t;

bool check_bitmap_format_support(const AndroidBitmapInfo &android_bitmap_info) {
    return android_bitmap_info.format == ANDROID_BITMAP_FORMAT_RGBA_8888
        || android_bitmap_info.format == ANDROID_BITMAP_FORMAT_RGB_565
        || android_bitmap_info.format == ANDROID_BITMAP_FORMAT_A_8;
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

