package com.victor.yunweatherkotlin

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.litepal.crud.ClusterQuery
import org.litepal.crud.DataSupport
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    var list = ArrayList<City>()

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
        Log.i("haha",s.absolutePath)
        setContentView(R.layout.activity_main)

        //异步
//        val handler = object :Handler(){
//            override fun handleMessage(msg: Message?) {
//                super.handleMessage(msg)
//
//            }
//        }

        et_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                toast("TextChanged")
                search(et_search.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                search(et_search.text.toString())
            }

        })
        bt_load.setOnClickListener{
            load()
        }
    }
//输入文本发生变化时，查询数据库并返回查询到的数据
    private fun search(input: String) {
        if(input.isEmpty()){
            list.clear()
        } else {
            val temp:List<City> =DataSupport.where("cityName like ?",input+"%").find(City::class.java)
            list.clear()
            list.addAll(temp)
            Log.i("haha",list.size.toString())
            for (i in list){
                Log.i("haha",i.cityName)
            }
            val c:City = DataSupport.findFirst(City::class.java)
            Log.i("haha",c.cityName)
        }
    }

    private fun load() {

        val isr = InputStreamReader(assets.open("china-city-list.txt"))
        val list = isr.readLines()
        var arrs: Array<String>? = null
        var num = 0
        for (i in list) {
            arrs = i.split("\t".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
//            Log.i("haha", arrs[0]+"    "+arrs[2])
            num++
            Log.i("haha", num.toString())
            var c = City(arrs[0], arrs[2])
            c.save()
            Log.i("haha",c.cityName+ c.cityCode)
        }
        tv_result.text = "Success"
    }

}


