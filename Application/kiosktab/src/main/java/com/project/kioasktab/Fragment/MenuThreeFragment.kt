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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.kioasktab.Adapter.MenuThreeAdapter
import com.project.kioasktab.Adapter.OptionThreeAdapter
import com.project.kioasktab.OrderActivity
import com.project.kioasktab.OrderData
import com.project.kioasktab.OrderSaved
import com.project.kioasktab.R
import com.project.kioasktab.databinding.FragmentMenuThreeBinding
import com.project.kioasktab.databinding.RecyclerMenu3Binding
import com.project.kioasktab.databinding.RecyclerOption3Binding

class MenuThreeFragment : Fragment(), MenuThreeAdapter.OnItemClickListener, OptionThreeAdapter.onOptionItemClickLIstener {
    lateinit var binding3: FragmentMenuThreeBinding
    lateinit var menuBinding3: RecyclerMenu3Binding
    lateinit var optionBinding3: RecyclerOption3Binding
    private var selectedItem:MenuItem? = null
    private var selectedOptionItem:OptionItem? = null

    class menuData3(var item3: String = "", var price3: Int = 0)
    class optionData3(var optionItem3: String = "", var optionPrice3: Int = 0)


    private lateinit var menuAdapter: MenuThreeAdapter
    data class MenuItem(val item3:String, val price3:Int)

    private lateinit var optionAdapter: OptionThreeAdapter
    data class OptionItem(val optionItem3: String, val optionPrice3: Int, var isChecked: Boolean = false)

    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    var currentMenuPrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.w("zio","fragment3")

        binding3 = FragmentMenuThreeBinding.inflate(inflater,container,false)
        menuBinding3 = RecyclerMenu3Binding.inflate(inflater,container,false)
        optionBinding3 = RecyclerOption3Binding.inflate(inflater,container,false)

        // DrawerLayout을 열기
        binding3.recyclerMenu3.setOnClickListener {
            binding3.drawerMenu3.openDrawer(GravityCompat.END)
        }

        //firebase에서 자료를 불러옴
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        MenuThreeFetch()
        OptionThreeFetch()

        // 주문하기 버튼 클릭 리스너 설정
        binding3.orderMenu3Item.setOnClickListener {view ->

            val selectedItem = this.selectedItem
            val selectOptions = optionAdapter.getSelecetOptions()
            val selectOptionItem = this.selectedOptionItem
            val optionSumPrice = selectOptions.sumBy { optionItem -> optionItem.optionPrice3 }

            if(selectedItem != null){
                val item3 = selectedItem.item3
                val price3 = selectedItem.price3
                val optionItem3 = selectOptionItem?.optionItem3
                val optionPrice3 = selectOptionItem?.optionPrice3 ?: 0

//                val selectOptionItemList = selectOptions.map { option ->
//                    OptionItemList(option.optionItem3, option.optionPrice3)
//                }.toMutableList()

                Log.w("zio","보내는 값3 : $item3, $price3")
                var existingItem = OrderSaved.orders.
                                        firstOrNull{ it.item == item3 && it.optionItem == optionItem3 }
                if (existingItem == null) {
                    OrderSaved.orders.add(OrderData(item3,price3+optionSumPrice,
                        optionItem3.toString(), optionPrice3, 1))
                } else {
                    existingItem.count += 1
                    existingItem.price += price3 + optionSumPrice
                }

               val intent = Intent(requireActivity(), OrderActivity::class.java)
                startActivity(intent)
            }
        }
        return binding3.root
    }

    //firebase에서 자료 불러오기
    private fun MenuThreeFetch(){

        db.collection("Menu3")
            .get()
            .addOnSuccessListener {result ->
                val menuList = mutableListOf<MenuItem>()
                val MenurecyclerView = binding3.recyclerMenu3

                for(document in result){
                    val menuData = document.toObject(menuData3::class.java)
                    menuList.add(MenuItem(menuData.item3,menuData.price3))
                    Log.d("zio","item3 : ${document.id} => data : ${document.data}")
                }
                menuAdapter = MenuThreeAdapter(menuList, this,this)
                MenurecyclerView.adapter = menuAdapter
                MenurecyclerView.layoutManager = GridLayoutManager(context,3)
                binding3.option3ItemPic.setImageResource(R.drawable.menu_icecoffee)
                binding3.option3ItemName.text = "테스트 품목3"
                binding3.option3ItemText.text = "설명 테스트3 입니다.\n얼마나 들어갈까요?"
            }
            .addOnFailureListener {e ->
                Log.w("zio","Error get document", e)
            }
    }

    private fun OptionThreeFetch(){

        db.collection("Option3")
            .get()
            .addOnSuccessListener {result ->
                val optionList = mutableListOf<OptionItem>()
                val OptionrecyclerView = binding3.recyclerOption3

                for(document in result){
                    val optionData = document.toObject(optionData3::class.java)
                    optionList.add(OptionItem(optionData.optionItem3, optionData.optionPrice3))
                    Log.d("zio","option3 : ${document.id} => data : ${document.data}")
                }
                optionAdapter = OptionThreeAdapter(optionList,this){position ->
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
        currentMenuPrice = menuItem.price3
        binding3.orderMenu3Total.text = "%,d원".format(currentMenuPrice)
        binding3.drawerMenu3.openDrawer(GravityCompat.END)
    }

    override fun onOptionItemClick(optionItem3: OptionItem) {
        selectedOptionItem = optionItem3
    }

    private fun updatePrice(optionList: MutableList<OptionItem>){
            var totalOptionPrice = 0

            for(option in optionList){
                if(option.isChecked){
                    totalOptionPrice += option.optionPrice3
                }
            }
            val currentMenuPrice = "%,d".format(selectedItem?.price3 ?:0).replace(",", "").toInt()
            binding3.orderMenu3Total.text = "%,d원".format(currentMenuPrice + totalOptionPrice)
        }
}

