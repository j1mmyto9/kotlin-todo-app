package com.example.kotlintodo.widget

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.example.kotlintodo.R
import com.example.kotlintodo.model.Todo

class TodoItemAdapter (context: Context, toDoList: MutableList<Todo>) : BaseAdapter() {

    private val mInflater:LayoutInflater = LayoutInflater.from(context)
    private var items = toDoList
    private var rowListener:TodoItemListener = context as TodoItemListener

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = items[position]
        val view:View
        val vh:ListRowHolder
        if(convertView == null){
            view = mInflater.inflate(R.layout.row_items, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        }else{
            view = convertView
            vh= view.tag as ListRowHolder
        }
        vh.title.text = item.title
        vh.done.isChecked = item.done

        vh.done.setOnClickListener{
            rowListener.onModifyTodoItem(item.id!!, !item.done)
        }
        vh.btnRemove.setOnClickListener{
            rowListener.onDeleteTodoItem(item.id!!)
        }

        return view
    }

    private class ListRowHolder(row:View?){
        val title = row!!.findViewById<TextView>(R.id.ri_textTitle)
        val done = row!!.findViewById<CheckBox>(R.id.ri_checkBox)
        val btnRemove = row!!.findViewById<ImageButton>(R.id.ri_imageButton)
    }

}