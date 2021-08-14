package com.example.kotlintodo

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.kotlintodo.databinding.ActivityMainBinding
import com.example.kotlintodo.model.Todo
import com.example.kotlintodo.widget.TodoItemAdapter
import com.example.kotlintodo.widget.TodoItemListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

class MainActivity : AppCompatActivity()  , TodoItemListener{

    private lateinit var binding: ActivityMainBinding
    val TAG = "TODO APP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener{
            addNewItemDialog()
        }

        initListView()

    }

    var collectionName = "todo"
    var db = FirebaseFirestore.getInstance()
    lateinit var  items: MutableList<Todo>
    lateinit var adapter: TodoItemAdapter

    private fun addNewItemDialog(){
        var alert = AlertDialog.Builder(this)
        val editText = EditText(this)
        alert.setMessage("Add new item")
        alert.setTitle("Enter to do item text")
        alert.setView(editText)
        alert.setPositiveButton("Submit"){dialog, positionButton ->
            var item = Todo.create()
            item.title = editText.text.toString()
            item.done = false
            db.collection(collectionName)
                .add(item.toMap())
                .addOnSuccessListener {
                    Log.d(TAG, "Document added with ID: " + it.id)
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error adding document: ", it)
                }

        }
        alert.show()

    }

    private fun initListView() {
        items = mutableListOf<Todo>()
        adapter = TodoItemAdapter(this, items)
        binding.itemsList.adapter = adapter
        // QuerySnapshot
        db.collection(collectionName).addSnapshotListener{ snapshot, e ->
            itemListener(snapshot, e)
        }
    }
    private  fun itemListener(snapshot: QuerySnapshot?, e:FirebaseFirestoreException?){
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return
        }
        items.clear()
        val data = snapshot?.documents
        if(data != null){
            for(e in data){
                var todo = Todo.create()
                todo.id = e.id
                todo.title = e.data?.get("title") as String?
                todo.done = e.data?.get("done") as Boolean? ?: false
                items.add(todo)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onModifyTodoItem(id: String, done: Boolean) {
        val user: MutableMap<String, Any> = HashMap()
        user["done"] = done
        db.collection(collectionName).document(id).set(user, SetOptions.merge())
    }

    override fun onDeleteTodoItem(id: String) {
        db.collection(collectionName).document(id).delete()
    }

}