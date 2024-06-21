package com.example.hydroheroapp.data.local.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hydroheroapp.R
import com.example.hydroheroapp.data.local.Reminder

class ReminderAdapter(
    private val reminders: MutableList<Reminder>,
    private val onItemClick: (Reminder) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.tv_time)
        val descriptionTextView: TextView = view.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_remider, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.timeTextView.text = reminder.time
        holder.descriptionTextView.text = reminder.description

        holder.itemView.setOnClickListener { onItemClick(reminder) }
    }

    override fun getItemCount() = reminders.size
}
