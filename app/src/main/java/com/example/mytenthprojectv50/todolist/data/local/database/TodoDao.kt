package com.example.mytenthprojectv50.todolist.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mytenthprojectv50.todolist.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy


@Dao
interface TodoDao {


    @Query("SELECT * FROM todos WHERE NOT isDeleted ORDER BY createdAt DESC")
    fun getTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE  isCompleted AND NOT isDeleted ORDER BY createdAt DESC")
    fun getCompletedTodos(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity): Long

    @Update
    suspend fun update(todo: TodoEntity)

    @Query("UPDATE todos SET isCompleted = 1 WHERE id = :id")
    suspend fun markAsCompleted(id: Long)

    @Query("UPDATE todos SET isCompleted = NOT isCompleted WHERE id = :id")
    suspend fun toggleCompleted(id: Long)

    @Query("UPDATE todos SET isDeleted = 1 WHERE id = :id")
    suspend fun delete(id: Long)

    @Query ("UPDATE todos SET isDeleted = 0 WHERE id = :id")
    suspend fun undoDelete(id: Long)

}