package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.adapter.EditTextAdapter
import com.victor.yunweatherkotlin.db.CityDB
import kotlinx.android.synthetic.main.activity_add_city.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.toast
import org.litepal.crud.DataSupport


class AddCityActivity : BaseActivity() {

    var list = ArrayList<CityDB>()
    private lateinit var mAdapter:EditTextAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        //设置点击事件
        setListener()
        //给recycler view添加适配器
//        setAdapter()
        setMAdapter()
        //设置文本变化监听
        setTextChangerListener()
    }

    /**设置适配器*/
    private fun setMAdapter() {
        mAdapter = EditTextAdapter(list)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter
        mAdapter.setOnItemClickListener { a, v, i ->
            val city = list[i]
            val cityCode = city.cityCode
            val edit = defaultSharedPreferences.edit()
            edit.putString("cityCode", cityCode)
            edit.putString("cityName",city.county)
            edit.apply()
            val i =Intent(applicationContext, WeatherActivity::class.java)
            i.putExtra("update",true)
            startActivity(i)
            finish()
        }
    }


    /**文本变化监听*/
    private fun setTextChangerListener() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search(et_search.text.toString())
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    /**点击事件*/
    private fun setListener() {
        iv_back_add.setOnClickListener { finish() }
        iv_location.setOnClickListener { toast("暂未实现") }
    }

    /**输入文本发生变化时，查询数据库并返回查询到的数据*/
    fun search(input: String) {
        //验证是否为空
        if (input.isEmpty()) {
            list.clear()
        } else {
            //不为空去查询数据库
            val temp: List<CityDB> = DataSupport.where("county like ?", "%$input%").find(CityDB::class.java)
            list.clear()
            list.addAll(temp)
            Log.i("haha", list.size.toString())
            for (i in list) {
                Log.i("haha", i.county)
            }

        }
    }


}
