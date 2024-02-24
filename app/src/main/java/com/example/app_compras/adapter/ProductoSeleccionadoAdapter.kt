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

class ProductoSeleccionadoAdapter(var listaSeleccionado: ArrayList<Product>, var context: Context): RecyclerView.Adapter<ProductoSeleccionadoAdapter.MyHolder>() {

    private lateinit var listener: OnRecyclerProductoSecondListener
    private lateinit var listaProductosSeleccionados: ArrayList<Product>

    init {
        listener = context as OnRecyclerProductoSecondListener
        listaProductosSeleccionados = listaSeleccionado
    }
    class MyHolder(item: View): RecyclerView.ViewHolder(item) {
        lateinit var imagen: ImageView
        lateinit var nombreProducto: TextView
        lateinit var precioProducto: TextView
        lateinit var botonEliminar: Button
        lateinit var categoriaProducto: String

        init {
            imagen = item.findViewById(R.id.item_image)
            nombreProducto = item.findViewById(R.id.item_nombre)
            precioProducto = item.findViewById(R.id.item_precio)
            botonEliminar = item.findViewById(R.id.boton_eliminar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val vista: View = LayoutInflater.from(context).inflate(R.layout.item_second, parent,false)
        return ProductoSeleccionadoAdapter.MyHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaProductosSeleccionados.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaProductosSeleccionados[position]
        Glide.with(context).load(producto.image).into(holder.imagen)
        holder.nombreProducto.text = producto.title
        holder.precioProducto.text = producto.price.toString() + "€."
        holder.botonEliminar.setOnClickListener() {
            listener.onProductoSeleccionadoSelected(producto)

        }
    }

    fun addProducto(producto: Product) {
        this.listaProductosSeleccionados.add(producto)
        notifyItemInserted(listaProductosSeleccionados.size -1)
    }

    interface OnRecyclerProductoSecondListener {
        fun onProductoSeleccionadoSelected(producto: Product) {

        }
    }
}
