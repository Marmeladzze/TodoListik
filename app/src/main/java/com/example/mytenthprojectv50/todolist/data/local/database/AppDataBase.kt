package com.example.mytenthprojectv50.todolist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytenthprojectv50.todolist.data.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

}