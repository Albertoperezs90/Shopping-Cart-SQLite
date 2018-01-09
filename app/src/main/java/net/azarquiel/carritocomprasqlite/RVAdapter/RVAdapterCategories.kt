package net.azarquiel.carritocomprasqlite.RVAdapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_categories.view.*
import net.azarquiel.carritocomprasqlite.ProductActivity
import net.azarquiel.carritocomprasqlite.model.Type

/**
 * Created by alberto on 09/11/2017.
 */
class RVAdapterCategories(val context: Context,
                          val layout : Int,
                          val dataList : List<Type>) : RecyclerView.Adapter<RVAdapterCategories.ViewHolder>() {


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

        fun bind(dataItem : Type){
            itemView.tvCategories.text = dataItem.type
            Picasso.with(context).load(dataItem.photo).into(itemView.ivCategory)

            itemView.setOnClickListener(View.OnClickListener {
                openCategory(dataItem)
            })
        }

        private fun openCategory(dataItem: Type) {
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("category", dataItem.type)
            val act = context as Activity
            act.startActivityForResult(intent,2)
        }
    }
}