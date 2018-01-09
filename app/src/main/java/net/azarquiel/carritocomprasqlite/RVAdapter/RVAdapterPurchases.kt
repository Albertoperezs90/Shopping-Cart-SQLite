package net.azarquiel.carritocomprasqlite.RVAdapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recycler_lastpurchases.view.*
import net.azarquiel.carritocomprasqlite.model.Purchase

/**
 * Created by alberto on 09/11/2017.
 */
class RVAdapterPurchases(val context : Context,
                         val layout : Int,
                         val dataList : List<Purchase>,
                         val dataPrice : ArrayList<Double>) : RecyclerView.Adapter<RVAdapterPurchases.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewLayout = layoutInflater.inflate(layout, parent, false)

        return ViewHolder(viewLayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(viewLayout : View, val context: Context) : RecyclerView.ViewHolder(viewLayout){
        fun bind(dataItem : Purchase){
            itemView.tvLastPurchasesDate.text = dataItem.date
            itemView.tvLastPurchasesPrice.text = dataPrice.get(0).toString()+"€"
            dataPrice.removeAt(0)
        }
    }
}