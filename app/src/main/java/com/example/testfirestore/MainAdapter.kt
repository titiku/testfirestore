package com.example.testfirestore

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user.view.*

class MainAdapter(private val updateUserListener: UpdateUserListener) :
    RecyclerView.Adapter<MainAdapter.UserViewHolder>() {
    private val list = arrayListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false),
            updateUserListener
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getList(): ArrayList<User> {
        return list
    }

    fun addList(user: User) {
        list.add(user)
        notifyDataSetChanged()
    }

    fun updateList(user: User) {
        list.filter { it.id == user.id }[0].progress =
            user.progress
        notifyDataSetChanged()
    }

    class UserViewHolder(
        itemView: View,
        var userListener: UpdateUserListener
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) {
            itemView.tvId.text = user.id
            itemView.tvName.text = user.name
            updateProgressBar(user)
            setOnIncreaseClick(user)
            setOnDecreaseClick(user)
        }

        private fun setOnIncreaseClick(user: User) {
            itemView.increase.apply {
                setOnClickListener {
                    user.progress += 20
                    if (user.progress > 100) {
                        user.progress = 100
                    }
                    userListener.updateProgressBar(user)
                }
            }
        }

        private fun setOnDecreaseClick(user: User) {
            itemView.decrease.apply {
                setOnClickListener {
                    user.progress -= 20
                    if (user.progress < 0) {
                        user.progress = 0
                    }
                    userListener.updateProgressBar(user)
                }
            }
        }

        private fun updateProgressBar(user: User) {
            itemView.progressBar.apply {
                ObjectAnimator.ofInt(this, "progress", user.progress).let {
                    it.duration = 800L
                    it.interpolator = AccelerateDecelerateInterpolator()
                    it.start()
                }
            }
        }
    }

    interface UpdateUserListener {
        fun updateProgressBar(data: User)
    }
}
