package br.edu.ufrn.android_ecommerce

data class CartItem (
        var product: Product? = null,
        var quantity: Int = 0)