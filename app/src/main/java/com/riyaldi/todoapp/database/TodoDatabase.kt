package com.riyaldi.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(version = 1, entities = [Todo::class])

abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDAO() : TodoDAO

    companion object{
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        @InternalCoroutinesApi
        fun getInstance(context: Context) : TodoDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}