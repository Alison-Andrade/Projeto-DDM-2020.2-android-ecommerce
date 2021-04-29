package br.edu.ufrn.android_ecommerce

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapter(var context: Context, var orders: List<Order>): RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private lateinit var textOrderID: TextView
        private lateinit var textOrderTotalPrice: TextView

        fun bindOrder(order: Order){
            textOrderID = itemView.findViewById(R.id.order_id)
            textOrderTotalPrice = itemView.findViewById(R.id.order_total_price)

            textOrderID.text = order.id
            textOrderTotalPrice.text = "$${order.totalPrice}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.order_info, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindOrder(orders[position])
    }

    override fun getItemCount(): Int = orders.size

}