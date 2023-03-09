#include <iostream>

#include "cosmos.h"

cosmos::cosmos(byte_t *byte_array, const array_size_t &size) {
    _byte_array = byte_array;
    _size = size;
    _bitmap = nullptr;
}

cosmos::~cosmos() {
    free(_byte_array);
    delete _bitmap;
}

byte_t *cosmos::get_byte_array() {
    return _byte_array;
}

array_size_t cosmos::get_size() const {
    return _size;
}
