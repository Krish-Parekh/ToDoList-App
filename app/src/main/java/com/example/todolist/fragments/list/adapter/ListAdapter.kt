package com.example.todolist.fragments.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.models.Priority
import com.example.todolist.data.models.ToDoData
import com.example.todolist.fragments.list.ListFragmentDirections

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var dataList = emptyList<ToDoData>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title_txt = itemView.findViewById<TextView>(R.id.title_txt)
        val des_txt = itemView.findViewById<TextView>(R.id.description_txt)
        val PriorityIndicator = itemView.findViewById<TextView>(R.id.priority_Indicator)
        val ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.row_background)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title_txt.text = dataList[position].title
        holder.des_txt.text = dataList[position].description
        holder.ConstraintLayout.setOnClickListener {
            val action =
                ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }

        when (dataList[position].priority) {
            Priority.HIGH -> holder.PriorityIndicator.backgroundTintList =
                ContextCompat.getColorStateList(holder.PriorityIndicator.context, R.color.red)
            Priority.MEDIUM -> holder.PriorityIndicator.backgroundTintList =
                ContextCompat.getColorStateList(holder.PriorityIndicator.context, R.color.yellow)
            Priority.LOW -> holder.PriorityIndicator.backgroundTintList =
                ContextCompat.getColorStateList(holder.PriorityIndicator.context, R.color.green)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList,toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}