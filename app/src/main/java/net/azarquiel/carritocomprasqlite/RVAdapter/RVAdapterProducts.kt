package net.azarquiel.carritocomprasqlite.RVAdapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.animation.AnimationUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_products.view.*
import net.azarquiel.carritocomprasqlite.ProductActivity
import net.azarquiel.carritocomprasqlite.R
import net.azarquiel.carritocomprasqlite.model.Product

/**
 * Created by alberto on 09/11/2017.
 */
class RVAdapterProducts (val context: Context,
                         val layout : Int,
                         val dataList : List<Product>) : RecyclerView.Adapter<RVAdapterProducts.ViewHolder> () {

    companion object {
        var contador : Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context) //Quiza de fallos, solucion (parent.context)
        val inflater = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(inflater, context)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }




    inner class ViewHolder (viewLayout : View, context : Context) : RecyclerView.ViewHolder(viewLayout){
        fun bind(dataItem : Product){
            itemView.tag = contador
            ++contador

            if (contador>3){
                contador = 1
            }


            Picasso.with(context).load(dataItem.image).into(itemView.ivProduct)
            if (dataItem.name.length > 25){
                val name : String = dataItem.name.substring(0,25)
            }
            itemView.tvProduct.text = dataItem.name

            itemView.setOnClickListener(View.OnClickListener {

                val tag = itemView.tag.toString().toInt()

                when (tag){
                    1 -> itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_product_1))
                    2 -> itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_product_2))
                    3 -> itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_product_3))
                }
                itemView.bringToFront()
                val intent = context as ProductActivity
                intent.addProductToList(dataItem)
                Log.d("Producto", "Compra de producto")
            })
        }
    }
}