package br.edu.ufrn.android_ecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminMenuActivity: AppCompatActivity() {

    private lateinit var signOutButton: Button
    private lateinit var buttonPedidos: Button
    private lateinit var btnAddProduct: Button
    private lateinit var textUsername: TextView
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_menu)

        textUsername = findViewById(R.id.textUsername)
        signOutButton = findViewById(R.id.button_signOut)

        buttonPedidos = findViewById(R.id.button_pedidos_admin)
        btnAddProduct = findViewById(R.id.btn_add_product)

        auth = FirebaseAuth.getInstance()
        textUsername.text = auth.currentUser.displayName

        btnAddProduct.setOnClickListener {
            startActivity(Intent(this, NewProductActivity::class.java))
        }

        buttonPedidos.setOnClickListener {
            startActivity(Intent(this, OrderListActivity::class.java))
        }

        signOutButton.setOnClickListener {
            auth.signOut()
            finish()
        }
    }

}