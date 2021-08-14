package com.example.kotlintodo.widget

interface TodoItemListener {
    fun onModifyTodoItem(id:String, done:Boolean)
    fun onDeleteTodoItem(id:String)
}