package net.azarquiel.carritocomprasqlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.content_main.*
import net.azarquiel.carritocomprasqlite.DBHelper.DBHelper
import net.azarquiel.carritocomprasqlite.GSONParse.AlcampoProductsRequest
import net.azarquiel.carritocomprasqlite.RVAdapter.RVAdapterProducts
import net.azarquiel.carritocomprasqlite.RVAdapter.RVAdapterPurchases
import net.azarquiel.carritocomprasqlite.model.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    private lateinit var db : DBHelper
    private lateinit var productList : List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        db = DBHelper(this)

        fab.setOnClickListener {  openActivityCategories() }

        btnPurchases.setOnClickListener( {loadRecyclerView()} )
    }

    private fun loadRecyclerView() {
        var cursor = db.getAllPurchases()
        val lista : ArrayList<Purchase> = arrayListOf()
        try {

            while (cursor.moveToNext()){
                val purchase : Purchase = Purchase(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("date")))
                lista.add(purchase)
            }
        } catch (e : Exception){

        }

        val priceList = arrayListOf<Double>()

        for (p in lista){
            val cursorDetails = db.getDetailsByIdPurchase(p.id)
            var price : Double = 0.0
            var quantity = 0
            try {

                do {
                    cursor = db.getProductPriceById(cursorDetails.getInt(cursorDetails.getColumnIndex("_idproduct")))
                    val idPurchase = p.id
                    val idProduct = cursor.getInt(cursor.getColumnIndex("_id"))
                    val quant = db.getQuantityByIdPurchaseAndIdProduct(idPurchase,idProduct)
                    quantity = quant.getInt(quant.getColumnIndex("quantity"))
                    price += cursor.getDouble(cursor.getColumnIndex("price"))
                }while (cursorDetails.moveToNext())

            } catch (e : Exception){
                e.printStackTrace()
            }

            priceList.add(price * quantity)
        }

        rvPurchases.layoutManager=LinearLayoutManager(this)
        rvPurchases.adapter = RVAdapterPurchases(this,R.layout.recycler_lastpurchases,lista, priceList)
    }

    private fun openActivityCategories() {

        val cursor = db.getAllCategories()

        val categories : ArrayList<Type> = ArrayList<Type>()

        try {
            while (cursor.moveToNext()){
                val name = cursor.getString(cursor.getColumnIndex("type"))
                val photo = cursor.getString(cursor.getColumnIndex("photo"))
                categories.add(Type(name,photo))
            }
        } catch (e : Exception){
            e.printStackTrace()
        }


        val intent = Intent(this@MainActivity, CategoriesActivity::class.java)

        intent.putExtra("categories", categories )

        startActivityForResult(intent,1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == 1 && data!=null){
            Log.d("Hola", "Alberto")
        }
    }

    private fun updatePurchaseList() {

    }


}
