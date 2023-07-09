package com.project.kioasktab.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kioasktab.Fragment.MenuThreeFragment
import com.project.kioasktab.databinding.RecyclerOption3Binding


open class OptionThreeAdapter(private val optionList: MutableList<MenuThreeFragment.OptionItem>,
                              private val optionListener: onOptionItemClickLIstener,
                              private val onItemCheckedChanged:(position: Int) ->Unit)
    : RecyclerView.Adapter<OptionThreeAdapter.OptionThreeViewHolder>() {

    interface onOptionItemClickLIstener{
        fun onOptionItemClick(optionItem3: MenuThreeFragment.OptionItem)
    }

        inner class OptionThreeViewHolder(val binding3: RecyclerOption3Binding) : RecyclerView.ViewHolder(binding3.root){

            init {
                binding3.root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        onItemCheckedChanged(position)
                        val optionItem3 = optionList[position]
                        optionListener.onOptionItemClick(optionItem3)
                    }
                }
            }
            fun bind(optionItem3: MenuThreeFragment.OptionItem){
                binding3.option3ItemItem.text = optionItem3.optionItem3
                binding3.option3ItemPrice.text = "%,d원".format(optionItem3.optionPrice3)

                binding3.option3ItemCheck.isChecked = optionItem3.isChecked
                binding3.option3ItemCheck.setOnCheckedChangeListener{_, isChecked->
                    optionItem3.isChecked = isChecked
                    onItemCheckedChanged(adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionThreeViewHolder {
            val binding3 = RecyclerOption3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            return OptionThreeViewHolder(binding3)
        }

        override fun getItemCount(): Int = optionList.size

        override fun onBindViewHolder(holder: OptionThreeViewHolder, position: Int) {
            holder.bind(optionList[position])

        //체크박스 누를시 recyclerView의 항목 체크 및 합계금액 동시에 변경
        holder.binding3.option3ItemCheck.setOnCheckedChangeListener { _, isChecked ->
            isChecked != isChecked
        }
    }

    fun getSelecetOptions(): List<MenuThreeFragment.OptionItem> {
        return optionList.filter { it.isChecked }
    }
}

