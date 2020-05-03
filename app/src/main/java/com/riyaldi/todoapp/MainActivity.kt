package com.riyaldi.todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riyaldi.todoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ToDoAdapter
    private lateinit var viewManager : RecyclerView.LayoutManager
    private lateinit var viewModel: TodoViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this ,R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ToDoAdapter(viewModel)
        recyclerView = binding.myRecyclerView

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.btTambah.setOnClickListener {
            if(binding.etNewTodo.text.isEmpty()) {
                Toast.makeText(this, "Kamu belum mengisi form", Toast.LENGTH_LONG).show()
            } else {
                viewModel.addTodo( binding.etNewTodo.text.toString().toUpperCase(Locale.ROOT))
                closeKeyboard()
                binding.etNewTodo.text = null
            }
        }

        viewModel.todos.observe(this, androidx.lifecycle.Observer { list ->
            viewAdapter.submitList(list.toMutableList())
        })
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}