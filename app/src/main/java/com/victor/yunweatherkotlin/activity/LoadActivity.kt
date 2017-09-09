package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.db.CityDB
import kotlinx.android.synthetic.main.activity_load.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 *   china-city-list:http://116.196.93.90/china-city-list.txt
 *
 * */

class LoadActivity : BaseActivity() {
    private var list = ArrayList<CityDB>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        loadFromNet()
        setListener()

    }

    private fun setListener() {
        bt_load.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
            finish()
        }
    }

    /**从网络导入查询数据库*/
    private fun loadFromNet(){
        doAsync {
            val citys :String = URL("http://116.196.93.90/china-city-list.txt").readText()
            val temp = citys.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var num = 0
            var progress = 0
            for (s in temp){
                var c = s.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                CityDB(c[0], c[2], c[9], c[7]).save()
                num++
                if (num in 1..3181 step 31){
                    uiThread { progressBar.progress = progress++ }
                }
            }
            val edit = defaultSharedPreferences.edit()
            edit.putBoolean("data",false).apply()
            uiThread {
                tv_load.text = "数据库升级完成"
                bt_load.visibility = View.VISIBLE
            }
        }
    }
}
