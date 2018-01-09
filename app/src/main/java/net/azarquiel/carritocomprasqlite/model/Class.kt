package net.azarquiel.carritocomprasqlite.model

import android.os.Parcelable
import java.io.Serializable


/**
 * Created by alberto on 09/11/2017.
 */

data class Purchase (var id : Int = 0, var date: String = "") : Serializable
data class Type (var type : String = "", var photo : String = "") : Serializable
data class Product (var id : Int = 0, var name : String = "", var price : Double = 0.0, var type : String = "", var image : String = "") : Serializable
data class Detail (var idPurchase : Int = 0, var idProduct : Int = 0, var quantity : Int = 0) : Serializable

data class AlcampoProducts(var result : List<Result>)
data class Result(var Category : String, var ProductTitle : String, var Image : String, var Price : String)
