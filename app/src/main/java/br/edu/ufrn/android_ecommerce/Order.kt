package br.edu.ufrn.android_ecommerce

data class Order(
        var id: String? = null,
        var user: String? = null,
        var cart: MutableList<CartItem>? = null,
        var totalPrice: Double? = null
)
