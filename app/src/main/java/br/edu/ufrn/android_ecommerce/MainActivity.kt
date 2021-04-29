package br.edu.ufrn.android_ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter

    private var products = listOf<Product>()

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolBar))

        val swipeRefreshLayout =  findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.design_default_color_primary))
        swipeRefreshLayout.isRefreshing = true

        findViewById<RecyclerView>(R.id.products_recyclerView).layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        getProducts()

        findViewById<RelativeLayout>(R.id.showCart).setOnClickListener{
            startActivity(Intent(this, ShoppingCartActivity::class.java))
        }

        swipeRefreshLayout.setOnRefreshListener {
            getProducts()
        }

    }

    override fun onStart() {
        super.onStart()

        findViewById<TextView>(R.id.cart_size).text = ShoppingCart.getShoppingCartSize().toString()

        var currentUser = auth.currentUser

        when {
            currentUser?.uid == "JMBeGDaBeDWeT0dpvNIa3DDb2zS2" -> {
                findViewById<ImageButton>(R.id.userButton).setOnClickListener{
                    startActivity(Intent(this, AdminMenuActivity::class.java))
                }
            }
            currentUser != null -> {
                findViewById<ImageButton>(R.id.userButton).setOnClickListener{
                    startActivity(Intent(this, UserMenuActivity::class.java))
                }
            }
            else -> {
                findViewById<ImageButton>(R.id.userButton).setOnClickListener{
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }
    }

    fun getProducts() {

        database = FirebaseDatabase
            .getInstance("https://android-ecommerce-d9d57-default-rtdb.firebaseio.com/")
            .getReference("produtos")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val productList = mutableListOf<Product>()

                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.id = productSnapshot.key

                        product?.let { productList.add(it) }
                        Log.d("SUCCESS", product?.image)
                    }

                    products = productList
                }

                findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).isRefreshing = false

                productAdapter = ProductAdapter(this@MainActivity, products)

                findViewById<RecyclerView>(R.id.products_recyclerView).adapter = productAdapter
                productAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}