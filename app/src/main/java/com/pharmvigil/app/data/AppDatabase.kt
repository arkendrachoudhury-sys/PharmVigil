package com.pharmvigil.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pharmvigil.app.data.dao.AeCaseDao
import com.pharmvigil.app.data.model.AeCase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [AeCase::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aeCaseDao(): AeCaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pharmvigil_database"
                )
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
