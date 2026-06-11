package com.productdemo.data.local.converters

import androidx.room.TypeConverter
import com.productdemo.data.local.entity.ReviewEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ProductConverters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val adapter: JsonAdapter<List<String>> = moshi.adapter(
            Types.newParameterizedType(List::class.java, String::class.java)
        )
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val adapter: JsonAdapter<List<String>> = moshi.adapter(
            Types.newParameterizedType(List::class.java, String::class.java)
        )
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromReviewList(value: List<ReviewEntity>): String {
        val adapter: JsonAdapter<List<ReviewEntity>> = moshi.adapter(
            Types.newParameterizedType(List::class.java, ReviewEntity::class.java)
        )
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toReviewList(value: String): List<ReviewEntity>? {
        val adapter: JsonAdapter<List<ReviewEntity>> = moshi.adapter(
            Types.newParameterizedType(List::class.java, ReviewEntity::class.java)
        )
        return adapter.fromJson(value)
    }
}
