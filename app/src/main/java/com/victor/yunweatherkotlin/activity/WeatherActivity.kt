package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import com.victor.yunweatherkotlin.R
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.title.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import android.support.v4.widget.SwipeRefreshLayout



/**
 * https://free-api.heweather.com/v5/weather?city=yourcity&key=yourkey
 *
 * key: ac4cceaa37be4d7099f04b2b0b456041
 *
 * such as : https://free-api.heweather.com/v5/weather?city=CN101191303&key=ac4cceaa37be4d7099f04b2b0b456041
 * */

class WeatherActivity : BaseActivity() {

    val key = "ac4cceaa37be4d7099f04b2b0b456041"
    var cityCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setLinstener()
        judgeIf()

    }
    /**欢迎页展示和显示天气*/
    private fun judgeIf() {
        //判断china-city-list是否有写到数据库，没有就欢迎启动导入数据库
        val sp = defaultSharedPreferences
        if (!sp.getBoolean("data", false)) {
            startActivity(Intent(this, LoadActivity::class.java))
            finish()
        }
        //判断是否有要查询的天气
        cityCode = sp.getString("cityCode", "")
        if (!"".equals(cityCode)) {
            showWeather()
        }
    }

    private fun setLinstener() {
        nav_button.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.edit_location -> startActivity(Intent(this, EditCitysActivity::class.java))
            }
            true
        }
        swipe_refresh.setOnRefreshListener{ showWeather() }
    }

    private fun showWeather() {
        if (!"".equals(cityCode)){
            doAsync {
                var url = "https://free-api.heweather.com/v5/weather?city=$cityCode&key=$key"
                val resultStr = URL(url).readText()
                uiThread {
                    tv_result.text = resultStr
                    swipe_refresh.isRefreshing = false
                }
            }
        }

    }
}
