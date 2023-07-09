package com.project.kioasktab.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kioasktab.Fragment.MenuTwoFragment
import com.project.kioasktab.databinding.RecyclerMenu2Binding

class MenuTwoAdapter(private val dataList: MutableList<MenuTwoFragment.MenuItem>,
                     val changePrice: MenuTwoFragment,
                     private val listener: OnItemClickListener) : RecyclerView.Adapter<MenuTwoAdapter.MenuTwoViewHolder>() {

    interface OnItemClickListener{

        fun onItemClick(menuitem: MenuTwoFragment.MenuItem)
    }

    inner class MenuTwoViewHolder(val binding2: RecyclerMenu2Binding) : RecyclerView.ViewHolder(binding2.root) {

        fun bind(item2: MenuTwoFragment.MenuItem){
            binding2.menu2itemItem.text = item2.item2
            binding2.menu2itemPrice.text = "%,d원".format(item2.price2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuTwoViewHolder {
        val binding2 = RecyclerMenu2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuTwoViewHolder(binding2)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MenuTwoViewHolder, position: Int) {
        val menuItem = getItem(position)
        holder.bind(menuItem)

        holder.itemView.setOnClickListener {
            listener.onItemClick(menuItem)
        }
    }

    fun getItem(position: Int): MenuTwoFragment.MenuItem{
        return dataList[position]
    }
}