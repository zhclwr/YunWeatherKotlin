package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.app.MyApplication
import com.victor.yunweatherkotlin.bean.City
import com.victor.yunweatherkotlin.db.CityDB
import kotlinx.android.synthetic.main.activity_load.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 *   china-city-list:http://116.196.93.90/china-city-list.txt
 *   上面服务器到期了。。
 *   改用本地资源
 *
 * */

class LoadActivity : BaseActivity() {
    private var list = ArrayList<CityDB>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        loadByGreenDao()
        setListener()

    }

    private fun setListener() {
        bt_load.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
            finish()
        }
    }

    /**从assets文件导入数据库*/
    private fun load(){
        doAsync {
            val ins = assets.open("china-city-list.txt")
            val reader = ins.reader()
            val list = reader.readLines()
            var num = 0
            var progress = 0
            for (s in list){
                val c = s.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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

    /**
     *通过greenDao从assets文件导入数据库
     */

    private fun loadByGreenDao(){
        doAsync {
            val ins = assets.open("china-city-list.txt")
            val reader = ins.reader()
            val list = reader.readLines()
            var num = 0
            var progress = 0
            val dao = MyApplication.getInstances().daoSession.cityDao
            var datas = ArrayList<City>()
            for (s in list){
                var c = s.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                datas.add(City(c[0], c[2], c[9], c[7]))
                num++
                //但是這裡的進度就不太準確了，恩 我知道
                if (num in 1..3181 step 31){
                    uiThread { progressBar.progress = progress++ }
                }
            }
            dao.insertInTx(datas)
            uiThread {
                tv_load.text = "数据库升级完成"
                bt_load.visibility = View.VISIBLE
            }
            defaultSharedPreferences.edit().putBoolean("data",false).apply()
        }
    }

    /**
     * 从网络导入查询数据库
     * 服务器到期，就不续费了。。。*/
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
