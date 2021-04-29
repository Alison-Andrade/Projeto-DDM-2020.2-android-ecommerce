package br.edu.ufrn.android_ecommerce

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

class NewProductActivity: AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var database: DatabaseReference

    private lateinit var btnChooseImage: Button
    private lateinit var btnNewProduct: Button
    private lateinit var imagePreview: ImageView

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextPrice: TextInputEditText
    private lateinit var editTextQuantity: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        imagePreview = findViewById(R.id.image_preview)
        btnChooseImage = findViewById(R.id.btn_choose_image)
        btnChooseImage.setOnClickListener { launchGallery() }

        btnNewProduct = findViewById(R.id.cadastrar_produto)
        btnNewProduct.setOnClickListener { cadastrarProduto() }

        editTextName = findViewById(R.id.editTextName)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextQuantity = findViewById(R.id.editTextQuantity)

    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagePreview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(name: String, price: Double, quantity: Int, uri: String){

        database = FirebaseDatabase
            .getInstance("https://android-ecommerce-d9d57-default-rtdb.firebaseio.com/")
            .getReference("produtos")

        val productId = database.push().key
        val product = Product(productId, name, price, quantity, uri)
        database.child(productId.toString()).setValue(product).addOnCompleteListener {
            Log.d("SUCCESS", productId)
            Toast.makeText(this, "Novo produto adcionado ao banco de dados", Toast.LENGTH_SHORT).show()

            finish()
        }
    }

    private fun cadastrarProduto(){

        var name = editTextName.text.toString().trim()
        var price = editTextPrice.text.toString().trim()
        var quantity = editTextQuantity.text.toString().trim()

        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(name, price.toDouble(), quantity.toInt(), downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

}