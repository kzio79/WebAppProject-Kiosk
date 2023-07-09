package com.project.kioasktab.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kioasktab.Fragment.MenuTwoFragment
import com.project.kioasktab.databinding.RecyclerOption2Binding

open class OptionTwoAdapter(private val optionList: MutableList<MenuTwoFragment.OptionItem>,
                            private val optionListener: onOptionItemClickListener,
                            private val onItemCheckedChanged:(position: Int) ->Unit)
    : RecyclerView.Adapter<OptionTwoAdapter.OptionTwoViewHolder>() {

    interface onOptionItemClickListener{
        fun onOptionItemClick(optionItem2: MenuTwoFragment.OptionItem)
    }

        inner class OptionTwoViewHolder(val binding2: RecyclerOption2Binding) : RecyclerView.ViewHolder(binding2.root){

            init {
                binding2.root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        onItemCheckedChanged(position)
                        val optionItem2 = optionList[position]
                        optionListener.onOptionItemClick(optionItem2)
                    }
                }
            }
            fun bind(optionItem2: MenuTwoFragment.OptionItem){
                binding2.option2ItemItem.text = optionItem2.optionItem2
                binding2.option2ItemPrice.text = "%,d원".format(optionItem2.optionPrice2)

                binding2.option2ItemCheck.isChecked = optionItem2.isChecked
                binding2.option2ItemCheck.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked != optionItem2.isChecked){
                        optionItem2.isChecked = isChecked
                        onItemCheckedChanged(adapterPosition)
                    }

                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionTwoViewHolder {
            val binding2 = RecyclerOption2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            return OptionTwoViewHolder(binding2)
        }

        override fun getItemCount(): Int = optionList.size

        override fun onBindViewHolder(holder: OptionTwoViewHolder, position: Int) {
            holder.bind(optionList[position])

        //체크박스 누를시 recyclerView의 항목 체크 및 합계금액 동시에 변경
        holder.binding2.option2ItemCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            optionList[position].isChecked != isChecked
        }
    }

    fun getSelecetOptions(): List<MenuTwoFragment.OptionItem> {
        return optionList.filter { it.isChecked }
    }
}