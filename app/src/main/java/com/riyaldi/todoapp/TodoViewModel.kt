package com.riyaldi.todoapp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.AndroidViewModel
import com.riyaldi.todoapp.database.Todo
import com.riyaldi.todoapp.database.TodoDAO
import com.riyaldi.todoapp.database.TodoDatabase
import com.riyaldi.todoapp.database.TodoRepository
import kotlinx.coroutines.*

@InternalCoroutinesApi
class TodoViewModel(application: Application) : AndroidViewModel(application) {
    // add repository
    private val repository: TodoRepository
    private val todoDAO: TodoDAO = TodoDatabase.getInstance(application).todoDAO()

    private var _todos : LiveData<List<Todo>>

    val todos: LiveData<List<Todo>>
        get() = _todos

    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.IO + vmJob)

    init {
        repository = TodoRepository(todoDAO)
        _todos = repository.allTodos
    }

    fun addTodo(text: String) {
        uiScope.launch {
            repository.insert(Todo(0, text))
        }
    }

    fun removeTodo(todo: Todo) {
        uiScope.launch {
            repository.delete(todo)
        }
    }

    fun updateTodo(id: Int, editedText: String) {
        uiScope.launch {
            repository.update(Todo(id, editedText))
        }
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}