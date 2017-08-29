package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.db.City
import kotlinx.android.synthetic.main.activity_load.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.InputStreamReader



class LoadActivity : BaseActivity() {
    private var list = ArrayList<City>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        load()
        setListener()
    }

    private fun setListener() {
        bt_load.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
            finish()
        }
    }
    /**导入数据库*/
    private fun load() {
        doAsync {
            val stream = InputStreamReader(assets.open("china-city-list.txt"))
            val temp = stream.readLines()
            var num = 0
            var progress = 0
            for (s in temp){
                var c = s.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                City(c[0], c[2], c[9], c[7]).save()
                num++
                if (num in 1..3181 step 31){
                    uiThread { progressBar.progress = progress++ }
                }
            }
            val edit = defaultSharedPreferences.edit()
            edit.putBoolean("data",true).apply()
            uiThread {
                tv_load.text = "数据库升级完成"
                bt_load.visibility = View.VISIBLE
            }
        }

    }
}
