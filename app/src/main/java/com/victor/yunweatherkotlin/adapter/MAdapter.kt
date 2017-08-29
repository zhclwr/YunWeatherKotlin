package com.victor.yunweatherkotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.victor.yunweatherkotlin.interfaces.OnItemClickListener
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.db.City
import org.jetbrains.anko.find


class MAdapter(val list:ArrayList<City>) : RecyclerView.Adapter<MAdapter.ViewHolder>() {

    var listener : OnItemClickListener? = null

    fun setOnClickListener(listener: OnItemClickListener){
        this.listener = listener
    }



    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val city = list[position]
        holder?.tv1?.text = city.county
        holder?.tv2?.text = city.province
        holder?.tv3?.text = city.city
        holder?.itemView?.tag = position
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item, parent, false))

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv1: TextView = itemView.find(R.id.tv1_item)
        var tv2: TextView = itemView.find(R.id.tv2_item)
        var tv3: TextView = itemView.find(R.id.tv3_item)

        init {

//            itemView.setOnClickListener { toast("asdasd    "+tv1.text+"   aaaaaaaa") }
            itemView.setOnClickListener { view ->
                if (listener != null) {
                    listener?.onItemClick(view, layoutPosition)
                }
            }
        }
    }


}