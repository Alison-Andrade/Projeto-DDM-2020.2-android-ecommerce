package br.edu.ufrn.android_ecommerce

import com.google.gson.annotations.SerializedName

data class Product (
        var id: String? = null,
        var name: String? = null,
        var price: Double? = null,
        var quantity: Int? = null,
        var image: String? = null
)