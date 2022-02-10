package com.example.dabf.ui
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dabf.R
import com.example.dabf.model.Ordenes

import kotlinx.android.synthetic.main.products_details.view.*


class OrdenesAdapter
    : RecyclerView.Adapter<OrdenesAdapter.ViewHolder>() {

    var ordenes = ArrayList<Ordenes>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         fun bind(ordenes: Ordenes) = with(itemView){
                 orderid.text = context.getString(R.string.item_orderid,ordenes.id)
                 estadoid.text = ordenes.status
                 tackingid.text = ordenes.traking
                 courierid.text = ordenes.courier
                 precioid.text = context.getString(R.string.item_money,ordenes.totalPrice)

        }

    }

    // Inflates XML items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                //Permite crear una vista atravez de unxml
                LayoutInflater.from(parent.context).inflate(R.layout.products_details,parent,false)

            )
    }

    // Binds Data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ordenes = ordenes[position]
        holder.bind(ordenes)
    }

    //Number of elements
    override fun getItemCount() = ordenes.size

}