package com.example.sleeptrackingproject

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.te)
}
