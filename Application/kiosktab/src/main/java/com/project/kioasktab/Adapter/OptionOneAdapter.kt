package com.project.kioasktab.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kioasktab.Fragment.MenuOneFragment
import com.project.kioasktab.databinding.RecyclerOption1Binding


open class OptionOneAdapter(val optionList: MutableList<MenuOneFragment.OptionItem>,
                            val optionListener: onOptionItemClickListener,
                            val onItemCheckedChanged:(position: Int) ->Unit)
    : RecyclerView.Adapter<OptionOneAdapter.OptionOneViewHolder>() {

    interface onOptionItemClickListener{
        fun onOptionItemClick(optionItem1: MenuOneFragment.OptionItem)
    }

        inner class OptionOneViewHolder(val binding1: RecyclerOption1Binding) : RecyclerView.ViewHolder(binding1.root){

//            init {
//                binding1.option1Item.setOnClickListener {
//                    val position = adapterPosition
//                    if(position != RecyclerView.NO_POSITION){
//                        onItemCheckedChanged(position)
//                        val optionItem1 = optionList[position]
//                        optionListener.onOptionItemClick(optionItem1)
//                    }
//                }
//            }
            fun bind(optionItem: MenuOneFragment.OptionItem){
                binding1.option1ItemItem.text = optionItem.optionItem1
                binding1.option1ItemPrice.text = "%,d원".format(optionItem.optionPrice1)

                binding1.option1ItemCheck.setOnCheckedChangeListener(null)


                binding1.option1Item.setOnClickListener(null)

//                binding1.option1ItemCheck.isChecked = optionItem.isChecked
                binding1.option1ItemCheck.setOnCheckedChangeListener  { _, isChecked ->
                    optionItem.isChecked = isChecked
                    onItemCheckedChanged(adapterPosition)
                }

                binding1.option1Item.setOnClickListener {
                    optionItem.isChecked = !optionItem.isChecked
                    binding1.option1ItemCheck.isChecked = optionItem.isChecked
                    onItemCheckedChanged(adapterPosition)
                    optionListener.onOptionItemClick(optionItem)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionOneViewHolder {
            val binding1 = RecyclerOption1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            return OptionOneViewHolder(binding1)
        }

        override fun getItemCount(): Int = optionList.size

        override fun onBindViewHolder(holder: OptionOneViewHolder, position: Int) {
            holder.bind(optionList[position])

        //체크박스 누를시 recyclerView의 항목 체크 및 합계금액 동시에 변경
//            holder.binding1.option1ItemCheck.setOnCheckedChangeListener { _, isChecked ->
//                isChecked != isChecked
//    //            uncheckPrice()
//            }
    }
//    fun uncheckPrice():Int {
//        val totalPrice = OrderSaved.orders.filter { it.isChecked }.sumOf { it.price }
//
//        FragmentMemuOneBinding.bind().orderMenu1Total.text ="%,d원".format(totalPrice)
//        return totalPrice
//    }

    fun getSelecetOptions(): List<MenuOneFragment.OptionItem> {
//        return optionList.filter { option -> option.isChecked }
        return optionList.filter { it.isChecked }
    }
}

