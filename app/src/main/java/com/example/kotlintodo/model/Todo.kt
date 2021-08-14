package com.example.kotlintodo.model

class Todo {
    companion object Factory{
        fun create(): Todo = Todo()
    }
    var id:String? = null
    var title:String? = null
    var done:Boolean = false
    fun toMap(): MutableMap<String, Any>{
        val user: MutableMap<String, Any> = HashMap()
        if(id != null) {
            user["id"] = id!!
        }
        user["title"] = title ?: ""
        user["done"] = done
        return user
    }
}