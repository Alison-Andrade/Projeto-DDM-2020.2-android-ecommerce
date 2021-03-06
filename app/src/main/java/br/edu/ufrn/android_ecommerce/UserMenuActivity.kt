package br.edu.ufrn.android_ecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UserMenuActivity: AppCompatActivity() {

    private lateinit var signOutButton: Button
    private lateinit var buttonPedidos: Button
    private lateinit var textUsername: TextView
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_user_menu)

        textUsername = findViewById(R.id.textUsername)

        buttonPedidos = findViewById(R.id.button_pedidos)

        signOutButton = findViewById(R.id.button_signOut)

        auth = FirebaseAuth.getInstance()
        textUsername.text = auth.currentUser.displayName

        buttonPedidos.setOnClickListener {
            startActivity(Intent(this, OrderListActivity::class.java))
        }

        signOutButton.setOnClickListener {
            auth.signOut()
            finish()
        }
    }

}