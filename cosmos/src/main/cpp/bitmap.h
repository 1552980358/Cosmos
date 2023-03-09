#ifndef COSMOS_BITMAP_H
#define COSMOS_BITMAP_H

#include <jni.h>

typedef uint32_t bitmap_size_t;
typedef int32_t bitmap_format_t;

typedef struct bitmap {

    bitmap_size_t width;

    bitmap_size_t height;

    bitmap_format_t format;

} bitmap_t;

#endif //COSMOS_BITMAP_H
