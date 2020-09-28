package com.example.testfirestore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.UserViewHolder>() {
    private val list = arrayListOf<HashMap<String, String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addList(hashMap: HashMap<String, String>) {
        list.add(hashMap)
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hashMap: HashMap<String, String>) {
            itemView.tvName.text = hashMap["name"]
        }
    }
}
