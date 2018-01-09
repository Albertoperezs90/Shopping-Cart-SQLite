package net.azarquiel.carritocomprasqlite.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import net.azarquiel.carritocomprasqlite.GSONParse.AlcampoProductsRequest
import net.azarquiel.carritocomprasqlite.model.Detail
import net.azarquiel.carritocomprasqlite.model.Result
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by alberto on 06/11/2017.
 */
class DBHelper (private val context : Context) {

    companion object {
        private val BASE_DATOS = "shopping_cart.sqlite"
        private val VERSION = 24 //Si cambiamos el numero de version borramos toda la base de datos y la creamos de nuevo

        private val SQL_CREATE_TABLE_PURCHASE = "CREATE TABLE \"purchase\" " +
                                              "(\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , " +
                                              "\"date\" VARCHAR NOT NULL )"

        private val SQL_CREATE_TABLE_PRODUCT = "CREATE TABLE \"product\" " +
                                               "(\"_id\" INTEGER NOT NULL  UNIQUE, " +
                                               "\"name\" VARCHAR NOT NULL, " +
                                               "\"price\" DOUBLE NOT NULL, " +
                                               "\"type\" VARCHAR NOT NULL, " +
                                               "\"image\" VARCHAR, " +
                                               "PRIMARY KEY (\"_id\", \"type\"), " +
                                               "FOREIGN KEY(type) references type(type))"

        private val SQL_CREATE_TABLE_DETAIL = "CREATE TABLE \"detail\" " +
                                              "(\"_idpurchase\" INTEGER NOT NULL, " +
                                              "\"_idproduct\" INTEGER NOT NULL, " +
                                              "\"quantity\" INTEGER NOT NULL , " +
                                              "PRIMARY KEY (\"_idpurchase\", \"_idproduct\"), " +
                                              "FOREIGN KEY(_idpurchase) references purchase(_id), " +
                                              "FOREIGN KEY(_idproduct) references product(_id))"

        private val SQL_CREATE_TABLE_TYPE = "CREATE TABLE \"type\" " +
                                            "(\"type\" VARCHAR PRIMARY KEY  NOT NULL UNIQUE, " +
                                            "\"photo\" VARCHAR)"

        private val insertTypes = ArrayList<String>()
    }


    private val DBAdapter : DatabaseHelper
    private var db : SQLiteDatabase? = null
    private var request = AlcampoProductsRequest("none")


    private inner class DatabaseHelper internal constructor(context: Context) : SQLiteOpenHelper(context, BASE_DATOS, null, VERSION){


        override fun onCreate(db: SQLiteDatabase) {
            try {
                db.execSQL(SQL_CREATE_TABLE_PURCHASE)
                db.execSQL(SQL_CREATE_TABLE_TYPE)
                db.execSQL(SQL_CREATE_TABLE_PRODUCT)
                db.execSQL(SQL_CREATE_TABLE_DETAIL)

                doAsync {
                    request = AlcampoProductsRequest("https://api.agenty.com/v1/output/5be8180a15?api-id=KGYJOA5GMW&api-key=b0f709ef7cef72790546f2143327cbc1&collection=1&v=3")
                    uiThread {
                        fillProductsList()
                        fillTypeList()
                    }
                }


            } catch (e : SQLException){
                e.printStackTrace()
            }
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS purchase")
            db.execSQL("DROP TABLE IF EXISTS product")
            db.execSQL("DROP TABLE IF EXISTS detail")
            db.execSQL("DROP TABLE IF EXISTS type")
            onCreate(db)
        }
    }

    init {
        DBAdapter = DatabaseHelper(context)
    }


    //Abrir la base de datos
    @Throws(SQLException::class)
    fun openW(){
        db = DBAdapter.writableDatabase
    }

    //
    @Throws(SQLException::class)
    fun openR(){
        db = DBAdapter.readableDatabase
    }

    //Cerrar la base de datos
    @Throws(SQLException::class)
    fun closeDB(){
        DBAdapter.close()
    }

    fun fillTypeList(){

        insertTypes.add("INSERT INTO \"type\" VALUES(Quesos,https://static.vix.com/es/sites/default/files/imj/imujer/C/C%C3%B3mo%20eliminar%20manchas%20blancas%20en%20las%20u%C3%B1as%20productos%20lacteos.jpg);")
        insertTypes.add("INSERT INTO \"type\" VALUES(Carne,https://biotrendies.com/wp-content/uploads/2015/07/ranking-de-carnes.jpg);")
        insertTypes.add("INSERT INTO \"type\" VALUES(Pescados y Mariscos,http://pescadosymariscos.consumer.es/sites/default/files/imagenes_seccion/3/consejos-presentacion.jpg);")
        insertTypes.add("INSERT INTO \"type\" VALUES(Charcutería,http://www.deliverum.com/img-trans/category/1200-charcuteria.jpg);")
        insertTypes.add("INSERT INTO \"type\" VALUES(Pastelería,http://www.lecaroz.com/sites/default/files/styles/producto_resume/public/pasteles.jpg?itok=LztRyDR6);")
        insertTypes.add("INSERT INTO \"type\" VALUES(Verduras y hortalizas,https://biotrendies.com/wp-content/uploads/2016/08/verduras-con-mas-vitamina-c.jpg);")
        insertTypes.add("INSERT INTO \"type\" VALUES(Frutas,https://biotrendies.com/wp-content/uploads/2015/06/frutas-hidratos.jpg);")
        insertTypes.add("INSERT INTO \"type\" VALUES(Panadería,http://www.webconsultas.com/sites/default/files/styles/encabezado_articulo/public/temas/pan_0.jpg?itok=l9-ckI6P);")

        insertTypeTable()
    }

    fun fillProductsList(){

        openW()

        var category : String = ""

        val products : List<Result> = request.alcampoProducts.result
        var id : Int = 1
        for (r in products){
            if (r.Category.equals("Carne")){
                category="Carne"
            }else if (r.Category.equals("Pescados y Mariscos")){
                category="Pescados y Mariscos"
            }else if (r.Category.equals("Frutas")){
                category="Frutas"
            }else if (r.Category.equals("Verduras y hortalizas")){
                category="Verduras y hortalizas"
            }else if (r.Category.equals("Charcutería")){
                category="Charcutería"
            }else if (r.Category.equals("Quesos")){
                category="Quesos"
            }else if (r.Category.equals("Panadería")){
                category="Panadería"
            }else if (r.Category.equals("Pastelería")){
                category="Pastelería"
            }

            r.Category = category

            insertProduct(r, id)

            ++id
        }

        closeDB()
    }

    fun insertTypeTable(){
        openW()
        for (i in 0..insertTypes.size - 1){
            val values = ContentValues()
            var data = insertTypes.get(i)
            data = data.substring(data.indexOf('(') + 1,data.lastIndexOf(')'))
            data.replace("\'".toRegex(),"")

            val st = StringTokenizer(data,",")

            values.put("type",st.nextToken())
            values.put("photo",st.nextToken())

            db!!.insert("type",null, values)
        }
        closeDB()
    }

    fun insertProduct(result : Result, id : Int) {

        val values = ContentValues()
        values.put("_id", id)
        values.put("name", result.ProductTitle)
        values.put("price",result.Price)
        values.put("type",result.Category)

        var image : String = result.Image

        image = image.substring(image.indexOf("=\"") + 2,image.indexOf("alt") - 2)

        values.put("image",image)

        db!!.insert("product", null, values)

    }

    fun insertPurchase(date : String) : Int {
        val id : Int
        openW()

        val values = ContentValues()
        //La id se mete sola
        values.put("date", date)

        //Insertando fila
        id = db!!.insert("purchase",null,values).toInt()
        closeDB()

        return id
    }

    fun insertDetail(listDetails : ArrayList<Detail>) {
        openW()

        for (d in listDetails){

            val values = ContentValues()
            values.put("_idpurchase",  d.idPurchase)
            values.put("_idproduct", d.idProduct)
            values.put("quantity", d.quantity)

            db!!.insert("detail",null,values)

        }


        closeDB()
    }



    fun getAllPurchases() : Cursor {
        openR()

        val cursor = db!!.query("purchase", arrayOf("id","date"),null,null,null,null,"id", null)
        cursor.moveToFirst()
        return cursor
    }

    fun getAllProducts() : Cursor {
        openR()

        val cursor = db!!.query("product", arrayOf("_id","name","price","type"),null,null,null,null,"_id", null)
        cursor.moveToFirst()
        closeDB()
        return cursor

    }

    fun getProductPriceById(id : Int) : Cursor{
        openR()

        val cursor = db!!.query("product", arrayOf("_id","price"), "_id=$id",null,null,null,null,null)
        cursor.moveToFirst()
        closeDB()
        return cursor
    }

    fun getAllCategories() : Cursor {

        openR()

        val cursor = db!!.query("type", arrayOf("type","photo"),null,null,null,null,null,null)
        cursor.moveToFirst()
        closeDB()
        return cursor

    }


    fun getDetailsByIdPurchase(idPurchase : Int) : Cursor {
        openR()

        val cursor = db!!.query("detail", arrayOf("_idpurchase","_idproduct","quantity"),"_idpurchase = ${idPurchase.toInt()}",null,null,null,"_idpurchase",null)
        cursor.moveToFirst()
        closeDB()
        return cursor
    }

    fun getProductsByCategory(category : String) : Cursor {
        openR()

        val cursor = db!!.query("product", arrayOf("_id","name","price","image"),"type='${category}'",null,null,null,"name",null)
        cursor.moveToFirst()
        closeDB()
        return cursor
    }

    fun getQuantityByIdPurchaseAndIdProduct(idPurchase : Int, idProduct : Int) : Cursor{

        openR()
        val cursor = db!!.query("detail", arrayOf("quantity"),"_idpurchase=${idPurchase.toInt()} AND _idproduct=${idProduct.toInt()}",null,null,null,"_idpurchase",null)
        cursor.moveToFirst()
        closeDB()
        return cursor
    }









}


