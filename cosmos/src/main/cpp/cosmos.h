#ifndef COSMOS_COSMOS_H
#define COSMOS_COSMOS_H

#include <jni.h>

#include "bitmap.h"

typedef jbyte byte;
typedef byte byte_t;

typedef int32_t array_size_t;

typedef class cosmos {

private:
    byte_t *_byte_array;
    array_size_t _size;
    bitmap_t *_bitmap;

public:
    cosmos(byte_t *, const array_size_t &);

    ~cosmos();

    byte_t *get_byte_array();

    array_size_t get_size() const;

} cosmos_t;


#endif //COSMOS_COSMOS_H
