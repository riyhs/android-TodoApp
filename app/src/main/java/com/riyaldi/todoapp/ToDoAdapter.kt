package com.riyaldi.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.riyaldi.todoapp.database.Todo
import com.riyaldi.todoapp.databinding.ListItemBinding
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class ToDoAdapter(private val viewModel: TodoViewModel) :
    ListAdapter<Todo, ToDoAdapter.MyViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.todoText.text = getItem(holder.adapterPosition).task

        // DELETE
        holder.btDel.setOnClickListener {
            viewModel.removeTodo(getItem(holder.adapterPosition))
        }

        // UPDATE
        holder.btEdit.setOnClickListener {
            val context = holder.itemView.context

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.edit_item, null)
            val prevText = getItem(holder.adapterPosition).task
            val ediText = view.findViewById<TextView>(R.id.et_edit)
            ediText.text = prevText

            // dialog
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Edit Item")
                .setView(view)
                .setPositiveButton("Update") { _, _ ->
                    val editedText = ediText.text.toString().toUpperCase(Locale.ROOT)
                    viewModel.updateTodo(getItem(holder.adapterPosition).id, editedText)
                    holder.todoText.text = editedText
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
            alertDialog.create().show()
        }
    }

    class MyViewHolder(binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val todoText = binding.tvListItem
        val btDel = binding.btDelete
        val btEdit = binding.btEdit
    }
}

class TodoDiffCallback: DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}