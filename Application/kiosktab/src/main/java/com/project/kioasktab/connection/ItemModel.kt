package com.project.kioask.model

data class ItemModel (
    var code:String,
    var name:String,
    var image:String,
    var content:String,
    var price:Int,
    var state:Boolean = false
)