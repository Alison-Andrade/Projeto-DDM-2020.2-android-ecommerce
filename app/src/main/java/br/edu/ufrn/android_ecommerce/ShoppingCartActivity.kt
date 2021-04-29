package br.edu.ufrn.android_ecommerce

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

class ShoppingCartActivity: AppCompatActivity() {

    lateinit var adapter: ShoppingCartAdapter
    private lateinit var buttonCheckout: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        buttonCheckout = findViewById(R.id.button_checkout)
        auth = FirebaseAuth.getInstance()

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        upArrow?.setColorFilter(
            ContextCompat.getColor(this, R.color.purple_500),
            PorterDuff.Mode.SRC_ATOP
        )
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        adapter = ShoppingCartAdapter(this, ShoppingCart.getCart())
        adapter.notifyDataSetChanged()

        findViewById<RecyclerView>(R.id.shopping_cart_recyclerView).adapter = adapter
        findViewById<RecyclerView>(R.id.shopping_cart_recyclerView).layoutManager = LinearLayoutManager(
            this
        )

        var totalPrice = ShoppingCart.getCart().fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(
            (cartItem.product?.price!!.toDouble())
        )}

        findViewById<TextView>(R.id.total_price).text = "$${totalPrice}"

        buttonCheckout.setOnClickListener {
            if (auth.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {

                database = FirebaseDatabase
                        .getInstance("https://android-ecommerce-d9d57-default-rtdb.firebaseio.com/")
                        .getReference("pedidos")

                val orderId = database.push().key
                val order = Order(orderId, auth.uid, ShoppingCart.getCart(), totalPrice)
                database.child(orderId.toString()).setValue(order).addOnCompleteListener {
                    Log.d("SUCCESS", orderId)

                    ShoppingCart.clearCart()
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}