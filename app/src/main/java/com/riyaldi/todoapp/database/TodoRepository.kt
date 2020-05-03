package com.riyaldi.todoapp.database

class TodoRepository(private val TodoDAO : TodoDAO) {
    val allTodos = TodoDAO.loadTodos()

    fun insert(todo: Todo){
        TodoDAO.insertTodo(todo)
    }

    fun delete(todo: Todo){
        TodoDAO.deleteTodo(todo)
    }

    fun update(todo: Todo){
        TodoDAO.updateTodo(todo)
    }
}