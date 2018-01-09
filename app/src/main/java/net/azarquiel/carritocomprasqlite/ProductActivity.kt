package net.azarquiel.carritocomprasqlite

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout

import kotlinx.android.synthetic.main.activity_product.*
import net.azarquiel.carritocomprasqlite.DBHelper.DBHelper
import net.azarquiel.carritocomprasqlite.RVAdapter.RVAdapterProducts
import net.azarquiel.carritocomprasqlite.model.Product
import java.io.Serializable

class ProductActivity : AppCompatActivity() {

    val purchaseList : MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val category : String = intent.getStringExtra("category")

        val cursor : Cursor = DBHelper(this).getProductsByCategory(category)

        var mutableProducts : MutableList<Product> = mutableListOf()

        try {
            while (cursor.moveToNext()){
                var product : Product = Product()
                product.id = cursor.getInt(cursor.getColumnIndex("_id"))
                product.name = cursor.getString(cursor.getColumnIndex("name"))
                product.price = cursor.getDouble(cursor.getColumnIndex("price"))
                product.image = cursor.getString(cursor.getColumnIndex("image"))
                mutableProducts.add(product)
            }
        } catch (e : Exception){
            e.printStackTrace()
        }

        val products : List<Product> = mutableProducts.toList()

        rvProducts.adapter = RVAdapterProducts(this ,R.layout.recycler_products,products)
        rvProducts.layoutManager = GridLayoutManager(this, 3)

    }

    override fun onBackPressed() {
        if (purchaseList.isEmpty()){
            val intent = Intent()
            intent.putExtra("Ok", 2)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }else {
            val bundle : Bundle = Bundle()
            bundle.putSerializable("list", purchaseList as Serializable)
            val intent : Intent = Intent()
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun addProductToList(product : Product){
        purchaseList.add(product)
        val bundle : Bundle = Bundle()
        bundle.putSerializable("list", purchaseList as Serializable)
        val intent : Intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
    }

}
