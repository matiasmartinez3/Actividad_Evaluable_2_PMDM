package com.example.app_compras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_compras.adapter.ProductoSeleccionadoAdapter
import com.example.app_compras.databinding.ActivityMain2Binding
import com.example.app_compras.model.Product
import com.google.android.material.snackbar.Snackbar

class MainActivity2 : AppCompatActivity(), ProductoSeleccionadoAdapter.OnRecyclerProductoSecondListener {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var listaProductosSeleccionados: ArrayList<Product>
    private lateinit var adaptadorProductoSeleccionado: ProductoSeleccionadoAdapter

    private var precioTotal = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar2)

        supportActionBar?.title = "Mi carrito"

        listaProductosSeleccionados =
            (intent.getSerializableExtra("listaProductos")as? ArrayList<Product>)!!
        adaptadorProductoSeleccionado =
            ProductoSeleccionadoAdapter(listaProductosSeleccionados, this)
        binding.recyclerCarrito.layoutManager = LinearLayoutManager(this)
        binding.recyclerCarrito.adapter = adaptadorProductoSeleccionado
        binding.resultadoCompras.text = actualizarPrecio().toString()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.second_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.confirmacion_compra ->{
                Snackbar.make(
                    binding.root,
                    "Compra confirmada por valor de ${precioTotal}€",
                    Snackbar.LENGTH_SHORT
                ).setAction("Cerrar") {
                    finish()
                }.show()
                listaProductosSeleccionados.removeAll(listaProductosSeleccionados)
                adaptadorProductoSeleccionado.notifyDataSetChanged()
                binding.resultadoCompras.text = "0"
            }
            R.id.cancelar_compra ->{
                Snackbar.make(
                    binding.root,
                    "Carrito vaciado con exito",
                    Snackbar.LENGTH_SHORT
                ).show()
                listaProductosSeleccionados.removeAll(listaProductosSeleccionados)
                adaptadorProductoSeleccionado.notifyDataSetChanged()
                binding.resultadoCompras.text = "0"
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onProductoSeleccionadoSelected(producto: Product) {
        val posicion = adaptadorProductoSeleccionado.itemCount

        listaProductosSeleccionados.remove(producto)
        adaptadorProductoSeleccionado.notifyDataSetChanged()

        val precioActual = binding.resultadoCompras.text.toString().toInt()

        var precioProduct: Int = producto.price
        var resultado = precioActual - precioProduct

        binding.resultadoCompras.text = resultado.toString()

    }

    override fun onBackPressed() {
        super.onBackPressed()

        val listaCarrito = listaProductosSeleccionados.clone() as ArrayList<Product>
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("listaCarrito", ArrayList(listaCarrito))
        startActivity(intent)
    }

    fun actualizarPrecio(): Int {
        for(i in 0 until adaptadorProductoSeleccionado.listaSeleccionado?.size!!) {
            var producto = adaptadorProductoSeleccionado.listaSeleccionado.get(i)
            var precio = producto.price
            precioTotal += precio
        }
        return precioTotal
    }
}