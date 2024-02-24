package com.example.app_compras.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_compras.R
import com.example.app_compras.model.Product

class ProductoAdapter (var listaProductos: ArrayList<Product>, var context: Context): RecyclerView.Adapter<ProductoAdapter.MyHolder>()  {

    private var listaFiltrada: ArrayList<Product> = ArrayList(listaProductos)

    private lateinit var listener: OnRecyclerProductoListener
    init {
        listener = context as OnRecyclerProductoListener
    }

    class MyHolder(item: View): RecyclerView.ViewHolder(item) {
        lateinit var imagen: ImageView
        lateinit var nombreProducto: TextView
        lateinit var precioProducto: TextView
        lateinit var botonAñadir: Button
        lateinit var categoriaProducto: String

        init {
            imagen = item.findViewById(R.id.item_image)
            nombreProducto = item.findViewById(R.id.item_nombre)
            precioProducto = item.findViewById(R.id.item_precio)
            botonAñadir = item.findViewById(R.id.item_boton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val vista: View = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)
        return MyHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaFiltrada[position]

        Glide.with(context).load(producto.image).into(holder.imagen)

        holder.nombreProducto.text = producto.title
        holder.precioProducto.text = producto.price.toString() + " €."
        holder.botonAñadir.setOnClickListener {
            listener.onProductoSelected(producto)
        }
    }

    fun addProduct(producto: Product) {
        listaProductos.add(producto)
        listaFiltrada.add(producto)
        notifyItemInserted(listaFiltrada.size -1)
    }

    fun filtrarLista(categoria: String) {
        listaFiltrada =
            if(categoria.isEmpty()){
                ArrayList(listaFiltrada)
            } else {
                ArrayList(
                    listaProductos.filter {
                        it.category.equals(categoria, ignoreCase = true)
                    }
                )
            }
        notifyDataSetChanged()
    }

    interface OnRecyclerProductoListener {
        fun onProductoSelected(producto: Product)
    }
}