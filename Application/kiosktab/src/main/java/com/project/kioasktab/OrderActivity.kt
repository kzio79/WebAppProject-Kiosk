package com.project.kioasktab

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.project.kioasktab.Adapter.ViewPageAdapter
import com.project.kioasktab.Fragment.CartFragment
import com.project.kioasktab.databinding.ActivityOrderBinding

class OrderActivity: AppCompatActivity() {

    lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.orderViewPager.adapter = ViewPageAdapter(this)
        binding.orderViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        pageChangeCallback()

        Log.w("zio","order")
        binding.Menu1.setOnClickListener {
            binding.orderViewPager.visibility = View.VISIBLE

            binding.orderViewPager.setCurrentItem(0,true)
            binding.fragMenu1.visibility = View.VISIBLE
            binding.fragMenu2.visibility = View.GONE
            binding.fragMenu3.visibility = View.GONE
            binding.fragCart.visibility = View.GONE
        }
        binding.Menu2.setOnClickListener {
            binding.orderViewPager.visibility = View.VISIBLE

            binding.orderViewPager.setCurrentItem(1,true)
            binding.fragMenu1.visibility = View.GONE
            binding.fragMenu2.visibility = View.VISIBLE
            binding.fragMenu3.visibility = View.GONE
            binding.fragCart.visibility = View.GONE
        }
        binding.Menu3.setOnClickListener {
            binding.orderViewPager.visibility = View.VISIBLE

            binding.orderViewPager.setCurrentItem(2,true)
            binding.fragMenu1.visibility = View.GONE
            binding.fragMenu2.visibility = View.GONE
            binding.fragMenu3.visibility = View.VISIBLE
            binding.fragCart.visibility = View.GONE
        }
        binding.Cart.setOnClickListener {
//            binding.orderViewPager.setCurrentItem(3,true)
            binding.orderViewPager.visibility = View.GONE
            val fragmentManager:FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            var cartFragment = CartFragment()
            transaction.replace(R.id.order_frame, cartFragment)
            transaction.commit()

            binding.fragMenu1.visibility = View.GONE
            binding.fragMenu2.visibility = View.GONE
            binding.fragMenu3.visibility = View.GONE
            binding.fragCart.visibility = View.VISIBLE
            CartFragment()
        }
    }

    fun pageChangeCallback(){
        binding.orderViewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.apply {
                    fragMenu1.visibility = View.GONE
                    fragMenu2.visibility = View.GONE
                    fragMenu3.visibility = View.GONE
                    fragCart.visibility = View.GONE
                }

                when(position){
                    0 -> binding.fragMenu1.visibility = View.VISIBLE
                    1 -> binding.fragMenu2.visibility = View.VISIBLE
                    2 -> binding.fragMenu3.visibility = View.VISIBLE
                    3 -> binding.fragCart.visibility = View.VISIBLE
                }
            }
        })
    }
}

