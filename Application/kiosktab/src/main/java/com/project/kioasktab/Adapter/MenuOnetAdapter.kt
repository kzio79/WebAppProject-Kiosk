package com.project.kioasktab.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kioasktab.Fragment.MenuOneFragment
import com.project.kioasktab.databinding.RecyclerMenu1Binding

class MenuOneAdapter(private val itemList: MutableList<MenuOneFragment.MenuItem>,
                     val changePrice: MenuOneFragment,
                     private val listener: OnItemClickListener) : RecyclerView.Adapter<MenuOneAdapter.MenuOneViewHolder>() {

    interface OnItemClickListener{

        fun onItemClick(menuitem: MenuOneFragment.MenuItem)
    }

    inner class MenuOneViewHolder(val binding1: RecyclerMenu1Binding) : RecyclerView.ViewHolder(binding1.root) {

        fun bind(item: MenuOneFragment.MenuItem){
            binding1.menu1itemItem.text = item.item1
            binding1.menu1itemPrice.text = "%,d원".format(item.price1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuOneViewHolder {
        val binding1 = RecyclerMenu1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuOneViewHolder(binding1)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: MenuOneViewHolder, position: Int) {
        val menuItem = getItem(position)
        holder.bind(menuItem)


        holder.itemView.setOnClickListener {
            listener.onItemClick(menuItem)
        }
    }

    fun getItem(position: Int): MenuOneFragment.MenuItem{
        return itemList[position]
    }
}