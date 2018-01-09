package net.azarquiel.carritocomprasqlite

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_product.*
import net.azarquiel.carritocomprasqlite.DBHelper.DBHelper
import net.azarquiel.carritocomprasqlite.RVAdapter.RVAdapterCategories
import net.azarquiel.carritocomprasqlite.RVAdapter.RVAdapterProducts
import net.azarquiel.carritocomprasqlite.model.Detail
import net.azarquiel.carritocomprasqlite.model.Product
import net.azarquiel.carritocomprasqlite.model.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CategoriesActivity : AppCompatActivity() {

   var productsList : ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val listArray : ArrayList<Type> = intent.getSerializableExtra("categories") as ArrayList<Type>

        val categories : List<Type> = listArray.toList()

        rvCategories.adapter = RVAdapterCategories(this,R.layout.recycler_categories,categories)
        rvCategories.layoutManager = GridLayoutManager(this, 2)

        btnFinishPurchase.setOnClickListener(View.OnClickListener {
            finishPurchase()
        })

    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("Ok", 1)
        setResult(1, intent)
        finish()
    }

    private fun finishPurchase() {
        val date : Date = Date()
        val sdf : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val fecha = sdf.format(date)
        val id = DBHelper(this).insertPurchase(fecha)

        val listDetails : ArrayList<Detail> = ArrayList<Detail>()

        for (p in productsList){
            val detail : Detail = Detail()

            val idProduct = p.id
            var quantity = 1

            for (d in listDetails){
                detail.idProduct = idProduct
                detail.idPurchase = id
                detail.quantity = quantity

                if (idProduct == d.idProduct){
                    quantity++
                }
            }

            if (quantity > 1){
                listDetails.elementAt(listDetails.size - 1).quantity++
            }else {
                listDetails.add(detail)
            }


        }

        if (!listDetails.isEmpty()){
            DBHelper(this).insertDetail(listDetails)

            val intent = Intent()
            intent.putExtra("Ok", 1)
            setResult(1, intent)
            finish()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val bundle : Bundle = data.extras

        if (bundle != null && resultCode==Activity.RESULT_OK){
            val lista : ArrayList<Product> = bundle.getSerializable("list") as ArrayList<Product>
            for (p in lista){
                productsList.add(p)
            }

            if (lista.size>0) layoutCategories.alpha = 1F
        }
    }

}
