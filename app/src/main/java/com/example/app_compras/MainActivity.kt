package com.example.app_compras

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.app_compras.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.Request.Method
import com.example.app_compras.adapter.ProductoAdapter
import com.example.app_compras.model.Product
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), ProductoAdapter.OnRecyclerProductoListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptadorProducto: ProductoAdapter
    private lateinit var listaProductos: ArrayList<Product>
    private var listaProductosSeleccionados = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaProductos = ArrayList()
        adaptadorProducto = ProductoAdapter(listaProductos, this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(findViewById(R.id.toolbar))

        rellenarLista()
        rellenarSpinner()

        binding.recyclerProductos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.recyclerProductos.adapter = adaptadorProducto

        binding.spinnerCategorias.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val categoriaSeleccionada = parent!!.getItemAtPosition(position).toString()
                adaptadorProducto.filtrarLista(categoriaSeleccionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_carrito -> {
                val datosRecibidos = intent.getSerializableExtra("listaCarrito") as? ArrayList<Product>
                val intent = Intent(this, MainActivity2::class.java)
                if(datosRecibidos != null) {
                    for (i in datosRecibidos.indices) {
                        val pro = datosRecibidos[i]
                        listaProductosSeleccionados.add(pro)
                    }
                    intent.putExtra("listaProductos", ArrayList(listaProductosSeleccionados))
                } else {
                    intent.putExtra("listaProductos", ArrayList(listaProductosSeleccionados))
                }
                startActivity(intent)
            }
        }
        return true
    }

    fun rellenarLista() {
        var peticion: JsonObjectRequest = JsonObjectRequest("https://dummyjson.com/products",
            {
                var products: JSONArray = it.getJSONArray("products")
                for(i in 0 until products.length()) {
                    val product = products.getJSONObject(i)
                    val producto: Product = Product(
                        product.getString("thumbnail"),
                        product.getString("title"),
                        product.getInt("price"),
                        product.getString("category")
                    )
                    adaptadorProducto.addProduct(producto)
                }
            }, {
                Snackbar.make(
                    binding.root,
                    "error en la lectura del archivo JSON",
                    Snackbar.LENGTH_SHORT
                ).show()
            })
        Volley.newRequestQueue(applicationContext).add(peticion)
    }

    private fun rellenarSpinner() {
        val categoriasList = ArrayList<String>()

        val peticion = JsonArrayRequest(
            Request.Method.GET,
            "https://dummyjson.com/products/categories",
            null,
            { response ->
                for (i in 0 until response.length()) {
                    val cat = response.getString(i)
                    categoriasList.add(cat)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriasList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategorias.adapter = adapter
            },
            { error ->
                Snackbar.make(
                    binding.root,
                    "Error en la lectura de categorías: ${error.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            })

        Volley.newRequestQueue(applicationContext).add(peticion)
    }

    override fun onProductoSelected(producto: Product) {
        listaProductosSeleccionados.add(producto)
    }
}