#ifndef COSMOS_BITMAP_H
#define COSMOS_BITMAP_H

#include <jni.h>
#include <android/bitmap.h>

typedef uint32_t bitmap_size_t;
typedef int32_t bitmap_format_t;

typedef struct bitmap {

    bitmap_size_t width;

    bitmap_size_t height;

    bitmap_format_t format;

} bitmap_t;

bool check_bitmap_format_support(const AndroidBitmapInfo &);

int get_bitmap_format_byte_size(const AndroidBitmapInfo &);

#endif //COSMOS_BITMAP_H
