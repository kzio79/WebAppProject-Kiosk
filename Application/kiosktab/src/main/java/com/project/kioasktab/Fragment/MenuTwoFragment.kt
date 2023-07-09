package com.project.kioasktab.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.project.kioasktab.Adapter.MenuTwoAdapter
import com.project.kioasktab.Adapter.OptionTwoAdapter
import com.project.kioasktab.OrderActivity
import com.project.kioasktab.OrderData
import com.project.kioasktab.OrderSaved
import com.project.kioasktab.R
import com.project.kioasktab.databinding.FragmentMenuTwoBinding
import com.project.kioasktab.databinding.RecyclerMenu2Binding
import com.project.kioasktab.databinding.RecyclerOption2Binding

class MenuTwoFragment : Fragment(), MenuTwoAdapter.OnItemClickListener, OptionTwoAdapter.onOptionItemClickListener {
    lateinit var binding2: FragmentMenuTwoBinding
    lateinit var menuBinding2: RecyclerMenu2Binding
    lateinit var optionBinding2: RecyclerOption2Binding
    private var selectedItem:MenuItem? = null
    private var selectedOptionItem:OptionItem? = null

    class menuData2(var item2: String = "", var price2: Int = 0)
    class optionData2(var optionItem2: String = "", var optionPrice2: Int = 0)

    private lateinit var menuAdapter: MenuTwoAdapter
    data class MenuItem(val item2:String, val price2:Int)

    private lateinit var optionAdapter: OptionTwoAdapter
    data class OptionItem(val optionItem2:String, val optionPrice2:Int, var isChecked: Boolean = false)

    lateinit var db : FirebaseFirestore
    var currentMenuPrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.w("zio","fragment2")

        binding2 = FragmentMenuTwoBinding.inflate(inflater,container,false)
        menuBinding2 = RecyclerMenu2Binding.inflate(inflater,container,false)
        optionBinding2 = RecyclerOption2Binding.inflate(inflater,container,false)

        // DrawerLayout을 열기
        binding2.recyclerMenu2.setOnClickListener {
            binding2.drawerMenu2.openDrawer(GravityCompat.END)
        }

        //firebase에서 자료를 불러옴
        db = FirebaseFirestore.getInstance()

        MenuTwoFetch()
        OptionTwoFetch()

        // 주문하기 버튼 클릭 리스너 설정
        binding2.orderMenu2Item.setOnClickListener {

            val selectedItem = this.selectedItem
            val selectedOption = this.selectedOptionItem
            val selectedOptions = optionAdapter.getSelecetOptions()
            val optionSumPrice = selectedOptions.sumBy { optionItem -> optionItem.optionPrice2 }

            Log.w("zio","option2: ${selectedOption?.optionItem2}")
            if(selectedItem != null){
                val item2 = selectedItem.item2
                val price2 = selectedItem.price2
//                val selectOptionItemList = selectedOptions.map { option ->
//                    OptionItemList(option.optionItem2, option.optionPrice2)
//                }.toMutableList()

                val optionItem2 = selectedOptionItem?.optionItem2
                val optionPrice2 = selectedOptionItem?.optionPrice2 ?:0

                Log.w("zio","option2-2: $selectedOption")

                Log.w("zio","보내는 값3 : $item2, $price2")

                var existingItem = OrderSaved.orders.firstOrNull{it.item == item2 && it.optionItem == optionItem2 }

                if (existingItem == null) {
                    OrderSaved.orders.add(OrderData(item2,price2+optionSumPrice,
                        optionItem2.toString(), optionPrice2, 1))
                    Log.w("zio","option2-3: $selectedOption")

                } else {
                    existingItem.count += 1
                    existingItem.price += price2 + optionSumPrice
                }

                val intent = Intent(requireActivity(), OrderActivity::class.java)
                startActivity(intent)
            }
        }
        return binding2.root
    }

    //firebase에서 자료 불러오기
    private fun MenuTwoFetch(){

        db.collection("Menu2")
            .get()
            .addOnSuccessListener {result ->
                val menuList = mutableListOf<MenuItem>()
                val MenurecyclerView = binding2.recyclerMenu2

                for(document in result){
                    val menuData = document.toObject(menuData2::class.java)
                    menuList.add(MenuItem(menuData.item2,menuData.price2))
                    Log.d("zio","item2 : ${document.id} => data : ${document.data}")
                }
                menuAdapter = MenuTwoAdapter(menuList, this,this)
                MenurecyclerView.adapter = menuAdapter
                MenurecyclerView.layoutManager = GridLayoutManager(context,3)
                binding2.option2ItemPic.setImageResource(R.drawable.menu_icecoffee)
                binding2.option2ItemName.text = "테스트 품목2"
                binding2.option2ItemText.text = "설명 테스트2 입니다.\n얼마나 들어갈까요?"

            }
            .addOnFailureListener {e ->
                Log.w("zio","Error get document", e)
            }
    }

    private fun OptionTwoFetch(){

        db.collection("Option2")
            .get()
            .addOnSuccessListener {result ->
                val optionList = mutableListOf<OptionItem>()
                val OptionrecyclerView = binding2.recyclerOption2

                for(document in result){
                    val optionData = document.toObject(optionData2::class.java)
                    optionList.add(OptionItem(optionData.optionItem2, optionData.optionPrice2))
                    Log.d("zio","item2 : ${document.id} => data : ${document.data}")
                }
                optionAdapter = OptionTwoAdapter(optionList, this){position ->

                    optionList[position].isChecked = !optionList[position].isChecked
                    optionAdapter.notifyItemChanged(position)

                    updatePrice(optionList)
                }
                OptionrecyclerView.adapter = optionAdapter
                OptionrecyclerView.layoutManager = LinearLayoutManager(context)
            }
            .addOnFailureListener {e ->
                Log.w("zio","Error get document", e)
            }
    }

    //리사이클러 뷰 클릭시 drawer활성화
    override fun onItemClick(menuItem: MenuItem) {

        selectedItem = menuItem
        currentMenuPrice = menuItem.price2
        binding2.orderMenu2Total.text = "%,d원".format(currentMenuPrice)
        binding2.drawerMenu2.openDrawer(GravityCompat.END)
    }

    override fun onOptionItemClick(optionItem2: OptionItem) {
        selectedOptionItem = optionItem2
    }

    private fun updatePrice(optionList: MutableList<OptionItem>){
        var totalOptionPrice = 0

        for(option in optionList){
            if(option.isChecked){
                totalOptionPrice += option.optionPrice2
            }
        }
        val currentMenuPrice = "%,d".format(selectedItem?.price2 ?:0).replace(",", "").toInt()
        binding2.orderMenu2Total.text = "%,d원".format(currentMenuPrice + totalOptionPrice)
    }
}