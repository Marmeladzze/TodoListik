package com.example.mytenthprojectv50.todolist.data.local.repository


import com.example.mytenthprojectv50.todolist.data.local.database.TodoDao
import com.example.mytenthprojectv50.todolist.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun getTodos(): Flow<List<TodoEntity>> = todoDao.getTodos()

    suspend fun insert(todo: TodoEntity): Long = todoDao.insert(todo)

    suspend fun markAsCompleted(id: Long) = todoDao.markAsCompleted(id)

    suspend fun toggleCompleted(id: Long) = todoDao.toggleCompleted(id)

    suspend fun delete(id: Long) = todoDao.delete(id)

    suspend fun update(todo: TodoEntity) = todoDao.update(todo)

    suspend fun undoDelete(id: Long) = todoDao.undoDelete(id)

}