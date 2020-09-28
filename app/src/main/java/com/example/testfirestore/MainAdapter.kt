package com.example.testfirestore

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.UserViewHolder>() {
    private val list = arrayListOf<User>()
    private var updateUserListener: UpdateUserListener? = null

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

    fun addList(user: User) {
        list.add(user)
        notifyDataSetChanged()
    }

    fun setListener(listener: UpdateUserListener) {
        updateUserListener = listener
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.tvId.text = user.id
            itemView.tvName.text = user.name
            updateProgressBar(user.progress)
        }

        private fun updateProgressBar(point: Int) {
            itemView.progressBar.apply {
                ObjectAnimator.ofInt(this, "progress", point).let {
                    it.duration = 800L
                    it.interpolator = AccelerateDecelerateInterpolator()
                    it.start()
                }
            }
        }
    }

    interface UpdateUserListener {
        fun decreaseProgress(data: User)
        fun increaseProgress(data: User)
        fun onUserDataChange(data: User)
    }
}
