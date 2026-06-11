package com.productdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.productdemo.data.local.converters.ProductConverters
import com.productdemo.data.local.dao.ProductDao
import com.productdemo.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
@TypeConverters(ProductConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao

    companion object {
        const val DATABASE_NAME = "product_db"
    }
}
