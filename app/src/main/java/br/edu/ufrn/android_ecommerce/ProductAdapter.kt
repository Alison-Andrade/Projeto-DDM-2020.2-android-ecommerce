package br.edu.ufrn.android_ecommerce

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

class ProductAdapter(var context: Context, var products: List<Product> = arrayListOf()) :
        RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_row_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ProductAdapter.ViewHolder, position: Int) {
        viewHolder.bindProduct(products[position])
    }

    override fun getItemCount(): Int = products.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindProduct(product: Product) {
            itemView.findViewById<TextView>(R.id.product_name).text = product.name
            itemView.findViewById<TextView>(R.id.product_price).text = "$${product.price.toString()}"
            Picasso.get().load(product.image).fit().into(itemView.findViewById<ImageView>(R.id.product_image))

            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {

                itemView.findViewById<ImageButton>(R.id.addToCartButton).setOnClickListener{ view ->
                    val item = CartItem(product)
                    ShoppingCart.addItem(item)
                    Snackbar.make(
                            (itemView.context as MainActivity).findViewById(R.id.coordinator),
                            "${product.name} added to your cart",
                            Snackbar.LENGTH_LONG
                    ).show()

                    it.onNext(ShoppingCart.getCart())
                }

                itemView.findViewById<ImageButton>(R.id.removeItemButton).setOnClickListener { view ->

                    val item = CartItem(product)

                    ShoppingCart.removeItem(item, itemView.context)

                    Snackbar.make(
                            (itemView.context as MainActivity).findViewById(R.id.coordinator),
                            "${product.name} removed from your cart",
                            Snackbar.LENGTH_LONG
                    ).show()

                    it.onNext(ShoppingCart.getCart())
                }
            }).subscribe { cart ->
                var quantity = 0

                cart.forEach{ cartItem ->
                    quantity += cartItem.quantity
                }

                (itemView.context as MainActivity).findViewById<TextView>(R.id.cart_size).text = quantity.toString()
                Toast.makeText(itemView.context, "Cart size $quantity", Toast.LENGTH_SHORT).show()

            }

            }
        }
    }