package br.edu.ufrn.android_ecommerce

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderListActivity: AppCompatActivity() {

    private var _orders = listOf<Order>()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var adapter: OrdersAdapter
    private lateinit var orderListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        auth = FirebaseAuth.getInstance()

        orderListRecyclerView = findViewById(R.id.order_list_recyclerView)
        orderListRecyclerView.layoutManager = LinearLayoutManager(this)

        getOrders()

    }

    fun getOrders(){
        database = FirebaseDatabase
                .getInstance("https://android-ecommerce-d9d57-default-rtdb.firebaseio.com/")
                .getReference("pedidos")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val orders = mutableListOf<Order>()

                    for (orderSnapshot in snapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        order?.id = orderSnapshot.key

                        if (auth.currentUser.uid == "JMBeGDaBeDWeT0dpvNIa3DDb2zS2") {
                            order?.let { orders.add(it) }
                        } else {
                            if (auth.currentUser.uid == order?.user) {
                                order?.let { orders.add(it) }
                            }
                        }

                        Log.d("SUCCESS", order?.id)
                    }

                    _orders = orders
                }

                adapter = OrdersAdapter(this@OrderListActivity, _orders)
                orderListRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}