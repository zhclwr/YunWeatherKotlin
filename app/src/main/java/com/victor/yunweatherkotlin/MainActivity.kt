package com.victor.yunweatherkotlin

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.litepal.crud.DataSupport
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    var list = ArrayList<City>()
    var adapter: MAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
            supportActionBar!!.hide()
        }
        val s = Environment.getExternalStorageDirectory()
        Log.i("haha", s.absolutePath)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        adapter = MAdapter()
        recycler_view.adapter = adapter
        adapter?.setOnClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, positon: Int) {
                val city = list[positon]
                Log.i("haha",positon.toString())
                toast(city.cityName)
            }
        })



        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                toast("TextChanged")
//                search(et_search.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search(et_search.text.toString())
                adapter?.notifyDataSetChanged()
            }

        })
        bt_load.setOnClickListener {
            load()
        }
    }

    /**输入文本发生变化时，查询数据库并返回查询到的数据*/
    private fun search(input: String) {
        //验证是否为空
        if (input.isEmpty()) {
            list.clear()
        } else {
            //不为空去查询数据库
            val temp: List<City> = DataSupport.where("cityName like ?", "%$input%").find(City::class.java)
            list.clear()
            list.addAll(temp)
            Log.i("haha", list.size.toString())
            for (i in list) {
                Log.i("haha", i.cityName)
            }

        }
    }

    /**把china-city-list里的数据读取到数据库*/
    private fun load() {
        val sp = defaultSharedPreferences
        val res = sp.getBoolean("data", false)
        if (res) {
            tv_result.text = "Success"
            return
        }

        /**使用kotlin的异步*/
        doAsync {
            val isr = InputStreamReader(assets.open("china-city-list.txt"))
            val list = isr.readLines()
            var arrs: Array<String>? = null
            var num = 0
            for (i in list) {
                arrs = i.split("\t".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                num++
                Log.i("haha", num.toString())
                var c = City(arrs[0], arrs[2], arrs[7], arrs[9])
                c.save()
            }
            val edit = defaultSharedPreferences.edit()
            edit.putBoolean("data", true)
            edit.apply()
            uiThread { tv_result.text = "Success" }
        }


    }

    interface OnItemClickListener{
        fun onItemClick(v:View,position:Int)
    }

    /**RecyclerView的Adapter*/
    inner class MAdapter : RecyclerView.Adapter<MAdapter.ViewHolder>() {

        var listener : OnItemClickListener? = null

        fun setOnClickListener(listener:OnItemClickListener){
            this.listener = listener
        }



        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val city = list[position]
            holder?.tv1?.text = city.cityName
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
                itemView.setOnClickListener {
                    View.OnClickListener {
                        fun onClick(v: View) {
                            listener?.onItemClick(v, v.tag as Int)
                            toast(tv1.text)
                        }
                    }
                }
                itemView.setOnClickListener { toast("asdasd    "+tv1.text+"   aaaaaaaa") }
                itemView.setOnClickListener { view ->
                    if (listener != null) {
                        listener?.onItemClick(view, layoutPosition)
                    }
                }
            }
        }


    }
}



