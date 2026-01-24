package com.example.mytenthprojectv50.todolist.ui.screen

import com.example.mytenthprojectv50.todolist.data.local.entity.TodoEntity
import com.example.mytenthprojectv50.todolist.data.local.repository.TodoRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TodoRepository
): ViewModel() {
    private val _todos = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todos: StateFlow<List<TodoEntity>> = _todos.asStateFlow()

    private val _showAddSheet = MutableStateFlow(false)
    val showAddSheet: StateFlow<Boolean> = _showAddSheet.asStateFlow()

    private val _lastDeletedTodoId = MutableStateFlow<Long?>(null)
    val lastDeletedTodoId: StateFlow<Long?> = _lastDeletedTodoId.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTodos().collect { list ->
                _todos.value = list.sortedByDescending { it.createdAt }
            }
        }
    }

    fun openAddSheet() {
        _showAddSheet.value = true
    }

    fun closeAddSheet() {
        _showAddSheet.value = false
    }

    fun addTodo(title: String, description: String = "") {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isBlank()) return

        viewModelScope.launch {
            val newTodo = TodoEntity(
                title = trimmedTitle,
                description = description.trim()
            )
            repository.insert(newTodo)
            closeAddSheet()
        }
    }

    fun restoreTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.insert(todo) // Используем insert, который просто добавит объект обратно в БД
        }
    }

    fun editTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    fun markAsCompleted(id: Long) {
        viewModelScope.launch {
            repository.markAsCompleted(id)
        }
    }

    fun toggleCompleted(id: Long) {
        viewModelScope.launch {
            repository.toggleCompleted(id)
        }
    }

    fun deleteTodo(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
            _lastDeletedTodoId.value = id
        }
    }

    fun undoDelete(id: Long){
        val id = _lastDeletedTodoId.value ?: return
        viewModelScope.launch {
            repository.undoDelete(id)
            _lastDeletedTodoId.value = null
        }
    }
}