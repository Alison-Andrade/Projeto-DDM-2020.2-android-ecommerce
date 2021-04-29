package br.edu.ufrn.android_ecommerce

import android.content.Context
import android.util.Log
import io.paperdb.Paper

class ShoppingCart {

    companion object {
        fun addItem(cartItem: CartItem) {
            val cart = ShoppingCart.getCart()

            val targetItem = cart.singleOrNull {it.product?.id == cartItem.product?.id}
                if (targetItem == null) {
                    cartItem.quantity++
                    cart.add(cartItem)
                }else {
                    targetItem.quantity++
                }
                ShoppingCart.saveCart(cart)
        }

        fun removeItem(cartItem: CartItem, context: Context) {
            val cart = ShoppingCart.getCart()

            val targetItem = cart.singleOrNull { it.product?.id == cartItem.product?.id }
            if (targetItem != null) {
                if (targetItem.quantity > 0) {
                    targetItem.quantity--
                    if (targetItem.quantity <= 0) {
                        cart.remove(targetItem)
                    }
                } else {
                    cart.remove(targetItem)
                }
            }

            ShoppingCart.saveCart(cart)
        }

        fun clearCart() {
            val cart = ShoppingCart.getCart()

            val i = cart.iterator()

            while (i.hasNext()) {
                val item = i.next()
                i.remove()
            }

//            cart.forEach {
//                cart.remove(it)
//            }

            ShoppingCart.saveCart(cart)
        }

        private fun saveCart(cart: MutableList<CartItem>) {
            Paper.book().write("cart", cart)
        }

        fun getCart(): MutableList<CartItem> {
            return Paper.book().read("cart", mutableListOf())
        }

        fun getShoppingCartSize(): Int {
            var cartSize = 0
            ShoppingCart.getCart().forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }
    }
}