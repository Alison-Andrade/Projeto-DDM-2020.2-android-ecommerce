package br.edu.ufrn.android_ecommerce

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ShoppingCartAdapter(var context: Context, var cartItems: List<CartItem>) : RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>(){

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(cartItem: CartItem) {
            Picasso.get().load("http://192.168.1.4:3333"+cartItem.product?.image).fit().into(itemView.findViewById<ImageView>(R.id.product_image))

            itemView.findViewById<TextView>(R.id.product_name).text = cartItem.product?.name
            itemView.findViewById<TextView>(R.id.product_price).text = "$${cartItem.product?.price }"
            itemView.findViewById<TextView>(R.id.product_quantity).text = cartItem.quantity.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

}