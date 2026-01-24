package com.example.mytenthprojectv50.todolist.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName =  "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false,
    val isDeleted: Boolean = false

)