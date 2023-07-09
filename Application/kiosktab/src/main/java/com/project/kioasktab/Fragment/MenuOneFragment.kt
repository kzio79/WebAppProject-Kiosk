package com.project.kioasktab.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.project.kioasktab.Adapter.MenuOneAdapter
import com.project.kioasktab.Adapter.OptionOneAdapter
import com.project.kioasktab.OrderActivity
import com.project.kioasktab.OrderData
import com.project.kioasktab.OrderSaved
import com.project.kioasktab.R
import com.project.kioasktab.databinding.CustomDialogBinding
import com.project.kioasktab.databinding.FragmentMemuOneBinding
import com.project.kioasktab.databinding.RecyclerMenu1Binding
import com.project.kioasktab.databinding.RecyclerOption1Binding

class MenuOneFragment : Fragment(), MenuOneAdapter.OnItemClickListener , OptionOneAdapter.onOptionItemClickListener{
    lateinit var binding1: FragmentMemuOneBinding
    lateinit var menuBinding1: RecyclerMenu1Binding
    lateinit var optionBinding1: RecyclerOption1Binding
    private lateinit var customDialogBinding: CustomDialogBinding
    private var selectedItem: MenuItem? = null
//    private var selectedOptionItem: OptionItem? =null
    private var selectedOptionItem: MutableList<OptionItem> = mutableListOf()

    class menuData1(var item1: String = "", var price1: Int = 0)
    class optionData1(var optionItem1: String = "", var optionPrice1: Int = 0)

    private lateinit var menuAdapter: MenuOneAdapter
    data class MenuItem(val item1: String, val price1: Int)

    private lateinit var optionAdapter: OptionOneAdapter
    data class OptionItem(val optionItem1: String, val optionPrice1: Int, var isChecked: Boolean = false)

    lateinit var db: FirebaseFirestore
    var currentMenuPrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.w("zio", "fragment1")

        binding1 = FragmentMemuOneBinding.inflate(inflater, container, false)
        menuBinding1 = RecyclerMenu1Binding.inflate(inflater, container, false)
        optionBinding1 = RecyclerOption1Binding.inflate(inflater, container, false)

        // DrawerLayout을 열기
        binding1.recyclerMenu1.setOnClickListener {
            binding1.drawerMenu1.openDrawer(GravityCompat.END)
        }

        //firebase에서 자료를 불러옴
        db = FirebaseFirestore.getInstance()

        MenuOneFetch()
        OptionOneFetch()

        // 주문하기 버튼 클릭 리스너 설정
        binding1.orderMenu1Item.setOnClickListener {

            val selectedItem = this.selectedItem
            val selectedOptionItem = this.selectedOptionItem
            val selectedOptions = optionAdapter.getSelecetOptions()
            val optionSumPrice = selectedOptions.sumBy { optionItem -> optionItem.optionPrice1 }

            if(selectedItem != null){
                val item1 = selectedItem.item1
                val price1 = selectedItem.price1
                val optionItem1 = selectedOptionItem.map { it.optionItem1 }
                val optionPrice1 = selectedOptionItem.map { it.optionPrice1 }

                Log.w("zio","보내는 값3 : $item1, $price1, $optionItem1, $optionPrice1")
                Log.w("zio","옵션 아이템1111111 : $optionItem1")

                var existingItem = OrderSaved.orders.
                                        firstOrNull{ it.item == item1 && it.optionItem.equals(optionItem1)}

                if (existingItem == null) {

                    OrderSaved.orders.add(OrderData(item1,price1+optionSumPrice, optionItem1.toString(),
                        optionPrice1.size, 1))

                    Log.w("zio","옵션 아이템2 : ${optionItem1.toString()}")
                    Log.w("zio","저장되는 값: ${OrderSaved.orders}")
                } else {
                    existingItem.count += 1
                    existingItem.price +=  price1 + optionSumPrice
                }

                val intent = Intent(requireActivity(), OrderActivity::class.java)
                startActivity(intent)
            }
        }
        return binding1.root
    }

    //firebase에서 자료 불러오기
    private fun MenuOneFetch() {

        db.collection("Menu1")
            .get()
            .addOnSuccessListener { result ->
                val menuList = mutableListOf<MenuItem>()
                val MenurecyclerView = binding1.recyclerMenu1

                for (document in result) {
                    val menuData = document.toObject(menuData1::class.java)
                    menuList.add(MenuItem(menuData.item1, menuData.price1))
                    Log.d("zio", "item1 : ${document.id} => data : ${document.data}")
                }
                menuAdapter = MenuOneAdapter(menuList, this,this)
                MenurecyclerView.adapter = menuAdapter
                MenurecyclerView.layoutManager = GridLayoutManager(context,3)
                binding1.option1ItemPic.setImageResource(R.drawable.menu_icecoffee)
                binding1.option1ItemName.text = "테스트 품목"
                binding1.option1ItemText.text = "설명 테스트 입니다.\n얼마나 들어갈까요?"
            }
            .addOnFailureListener { e ->
                Log.w("zio", "Error get document", e)
            }
    }

    private fun OptionOneFetch() {

        db.collection("Option1")
            .get()
            .addOnSuccessListener { result ->
                val optionList = mutableListOf<OptionItem>()
                val OptionrecyclerView = binding1.recyclerOption1

                for (document in result) {
                    val optionData = document.toObject(optionData1::class.java)
                    optionList.add(OptionItem(optionData.optionItem1, optionData.optionPrice1))
                    Log.d("zio", "optionItem1 : ${document.id} => data : ${document.data}")
                }
                optionAdapter = OptionOneAdapter(optionList,this) { position ->
                    optionList[position].isChecked = !optionList[position].isChecked
                    optionAdapter.notifyItemChanged(position)

                    selectedOptionItem.clear()
                    selectedOptionItem.addAll(optionList.filter { it.isChecked })
                    updatePrice(optionList)
                }
                OptionrecyclerView.adapter = optionAdapter
                OptionrecyclerView.layoutManager = LinearLayoutManager(context)
            }
            .addOnFailureListener { e ->
                Log.w("zio", "Error get document", e)
            }
    }

    fun dialog() {
        val alertDialog = context?.let {
            AlertDialog.Builder(it)
                .setView(customDialogBinding.customDialog)
                .create()
        }

        val dialogTitle = customDialogBinding.dialogTitle
        val dialogMessage = customDialogBinding.dialogMessage
        val dialogBtn = customDialogBinding.dialogButton

        dialogTitle.text = "주문을 해주세요"
        dialogMessage.visibility = View.GONE
        dialogBtn.visibility = View.GONE

        alertDialog?.show()
    }

    //리사이클러 뷰 클릭시 drawer활성화
    override fun onItemClick(menuItem: MenuItem) {

        selectedItem = menuItem
        currentMenuPrice = menuItem.price1
        binding1.orderMenu1Total.text = "%,d원".format(currentMenuPrice)
        binding1.drawerMenu1.openDrawer(GravityCompat.END)

    }

    override fun onOptionItemClick(optionItem1: OptionItem) {
        if(!selectedOptionItem.equals(optionItem1)){
            selectedOptionItem.add(optionItem1)
        }else{
            selectedOptionItem.remove(optionItem1)
        }
//        updatePrice(optionList = selectedOptionItem)

    }

    fun updatePrice(optionList:List<OptionItem>){
        var totalOptionPrice = 0

        for(option in optionList){
            if(option.isChecked){
                totalOptionPrice += option.optionPrice1
            }
        }
        val currentMenuItemPrice = "%,d".format(selectedItem?.price1 ?:0).replace(",", "").toInt()
        binding1.orderMenu1Total.text = "%,d원".format(currentMenuItemPrice + totalOptionPrice)
    }
}