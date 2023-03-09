#include "lib_cosmos_util.h"

jfieldID get_pinter_field(JNIEnv *env, lib_cosmos_t thiz) {
    auto clazz = env->GetObjectClass(thiz);
    return env->GetFieldID(clazz, "pointer", " L");
}

cosmos_t *get_pointer(JNIEnv *env, lib_cosmos_t thiz, jfieldID pointer_field) {
    return (cosmos_t *) env->GetLongField(thiz, pointer_field);
}

void set_pointer(JNIEnv *env, lib_cosmos_t thiz, jfieldID pointer_field, pointer_t pointer) {
    // Delete first
    delete get_pointer(env, thiz, pointer_field);
    env->SetLongField(thiz, pointer_field, pointer);
}