package net.azarquiel.carritocomprasqlite.GSONParse

import com.google.gson.Gson
import net.azarquiel.carritocomprasqlite.model.AlcampoProducts
import java.net.URL

/**
 * Created by alberto on 11/11/2017.
 */
class AlcampoProductsRequest (url : String) {

    lateinit var alcampoProducts : AlcampoProducts

    init {
        if (!url.equals("none")){
            val jsonStr = URL(url).readText(charset("UTF-8"))
            alcampoProducts = Gson().fromJson(jsonStr, AlcampoProducts::class.java)
        }
    }
}