package com.mertcaliskanyurek.bootstrap.datastore

sealed class StorageKey<T>(val name: String) {
    class StringKey(name: String) : StorageKey<String>(name)
    class IntKey(name: String) : StorageKey<Int>(name)
    class BooleanKey(name: String) : StorageKey<Boolean>(name)
    class LongKey(name: String) : StorageKey<Long>(name)
    class FloatKey(name: String) : StorageKey<Float>(name)
    class DoubleKey(name: String) : StorageKey<Double>(name)
}
