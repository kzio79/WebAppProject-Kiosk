package com.project.kioasktab.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.kioasktab.Fragment.MenuThreeFragment
import com.project.kioasktab.databinding.RecyclerMenu3Binding

class MenuThreeAdapter(private val dataList: MutableList<MenuThreeFragment.MenuItem>,
                       val changePrice: MenuThreeFragment,
                       private val listener: OnItemClickListener) : RecyclerView.Adapter<MenuThreeAdapter.MenuThreeViewHolder>() {

    interface OnItemClickListener{

        fun onItemClick(menuitem: MenuThreeFragment.MenuItem)
    }

    inner class MenuThreeViewHolder(val binding3: RecyclerMenu3Binding) : RecyclerView.ViewHolder(binding3.root) {
        private val menu3ItemTextView: TextView = binding3.menu3itemItem
        private val menu3PriceTextView: TextView = binding3.menu3itemPrice

        fun bind(item3: MenuThreeFragment.MenuItem){
            menu3ItemTextView.text = item3.item3
            menu3PriceTextView.text = "%,d원".format(item3.price3)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuThreeViewHolder {
        val binding3 = RecyclerMenu3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuThreeViewHolder(binding3)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MenuThreeViewHolder, position: Int) {
        val menuItem = getItem(position)
        holder.bind(menuItem)

        holder.itemView.setOnClickListener {
         listener.onItemClick(menuItem)
        }
    }

    fun getItem(position: Int): MenuThreeFragment.MenuItem{
        Log.w("zio", "position : ${dataList[position]}")
        return dataList[position]
    }
}