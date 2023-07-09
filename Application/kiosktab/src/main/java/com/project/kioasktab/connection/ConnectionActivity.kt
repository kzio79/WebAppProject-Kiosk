package com.project.kioasktab.connection

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.project.kioask.model.ItemModel
import com.project.kioask.retrofit.NetworkService
import com.project.kioasktab.databinding.ActivityConnectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityConnectionBinding

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        coroutineScope.launch {
//        val query = "SELECT * FROM menu1"
//        val resultSet = DbManager.executeQuery(query)
//
//        try {
//            // 결과 사용 예시:
//            resultSet?.let {
//                while (it.next()) {
//                    val item = it.getString("item")
//                    val price = it.getInt("price")
//                    // 필요한 작업 수행
//
//                    Log.w("zio","item : $item, price: $price")
//                }
//                it.close()
//            }
//        }catch (e:Exception){
//            Log.w("zio","error : ${e.message}")
//        }
//        }
        val header = "lkhadslfkh32falke"
        val retrofit = Retrofit.Builder()
            .baseUrl("http://")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        val service = retrofit.create(NetworkService::class.java)
        val call: Call<List<ItemModel>> = service.doGetList()

        call?.enqueue(object : Callback<List<ItemModel>> {
            override fun onResponse(
                call: Call<List<ItemModel>>,
                response: Response<List<ItemModel>>
            ) {
                val list = response?.body()
                for(i in 0 until list!!.size){
                    Log.d("zio","${list.get(i).code}, ${list.get(i).name}")
                }
            }

            override fun onFailure(call: Call<List<ItemModel>>, t: Throwable) {
                Log.e("zio", "에러!@@@@@@@@@@@: ${t}")
            }
        })

    }
}
