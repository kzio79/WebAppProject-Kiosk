package com.project.kioask.retrofit

import com.project.kioask.model.ItemModel
import retrofit2.Call
import retrofit2.http.GET

interface NetworkService {
    
    @GET("/item/getlist")
        fun doGetList() : Call<List<ItemModel>>

}
