package com.victor.yunweatherkotlin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.db.County
import com.victor.yunweatherkotlin.gson.HeWeather
import com.victor.yunweatherkotlin.gson.Weather
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.suggestion.*
import kotlinx.android.synthetic.main.title.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.crud.DataSupport
import java.net.URL


/**
 * https://free-api.heweather.com/v5/weather?city=yourcity&key=yourkey
 *
 * key: ac4cceaa37be4d7099f04b2b0b456041
 *
 * such as : https://free-api.heweather.com/v5/weather?city=CN101191303&key=ac4cceaa37be4d7099f04b2b0b456041
 *
 * bingPic: http://guolin.tech/api/bing_pic
 * */

class WeatherActivity : BaseActivity() {

    private val key = "ac4cceaa37be4d7099f04b2b0b456041"
    private lateinit var cityCode: String
    private lateinit var weatherStr: String
    private lateinit var sp: SharedPreferences
    private var firstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        sp = defaultSharedPreferences
        setLinstener()
        judgeIf()
        requestWeather()
        bingPic()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.getBooleanExtra("update", false)!!) {
            swipe_refresh.isRefreshing = true
            requestWeather()
        }
    }

    private fun bingPic() {
        doAsync {
            val imageUrl = URL("http://guolin.tech/api/bing_pic").readText()
            uiThread {
                Glide.with(applicationContext).load(imageUrl).into(iv_bing)
            }
        }
    }

    /**欢迎页展示和显示天气*/
    private fun judgeIf() {
        //判断china-city-list是否有写到数据库，没有就欢迎启动导入数据库
        if (sp.getBoolean("data", true)) {
            startActivity(Intent(this, LoadActivity::class.java))
            finish()
        }
        //判断是否有缓存的天气
        if ("" != sp.getString("weatherStr", "")) {
            showWeather()
        } else {
            can_gone.visibility = View.GONE
            title_city.text = "暂无天气"
            image_add.visibility = View.VISIBLE
        }
        cityCode = sp.getString("cityCode", "")
        if ("" != cityCode) {
            requestWeather()
        }

    }

    private fun showWeather() {
        weatherStr = sp.getString("weatherStr", "")
        if (weatherStr == "") return
        else {
            can_gone.visibility = View.VISIBLE
            image_add.visibility = View.GONE
            val gson = Gson()
            val weather: Weather = gson.fromJson(weatherStr, Weather::class.java)
            val heWeather = weather.HeWeather5[0]
            saveCounty(heWeather)
            title_city.text = heWeather.basic.city
            title_update_time.text = "更新时间" + heWeather.basic.update.loc.split(" ")[1]
            degree_text.text = heWeather.now.tmp + "℃"
            weather_info_text.text = heWeather.now.cond.txt
            aqi_text.text = heWeather.aqi.city.aqi
            pm25_text.text = heWeather.aqi.city.pm25
            comfort_text.text = "舒适度：" + heWeather.suggestion.comf.txt
            car_wash_text.text = "洗车建议：" + heWeather.suggestion.cw.txt
            sport_text.text = "运动建议：" + heWeather.suggestion.sport.txt
            forecast_layout.removeAllViews()
            for ((date, tmp, cond) in heWeather.daily_forecast) {
                val view = LayoutInflater.from(applicationContext).inflate(R.layout.forecast_item, forecast_layout, false)
                val dateText = view.findViewById(R.id.date_text) as TextView
                val infoText = view.findViewById(R.id.info_text) as TextView
                val maxText = view.findViewById(R.id.max_text) as TextView
                val minText = view.findViewById(R.id.min_text) as TextView
                dateText.text = date
                infoText.text = cond.txt_d
                maxText.text = tmp.max
                minText.text = tmp.min
                forecast_layout.addView(view)
            }
        }
        swipe_refresh.isRefreshing = false
    }

    private fun saveCounty(weather: HeWeather) {
        val county = County(weather.basic.city, weather.now.cond.txt)
        val counties = DataSupport.findAll(County::class.java)
        if (county in counties) return else county.save()


    }

    private fun setLinstener() {
        nav_button.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.edit_location -> {
                    startActivity(Intent(this, EditCitysActivity::class.java))
                }
            }
            true
        }
        swipe_refresh.setOnRefreshListener {
            requestWeather()
            bingPic()
        }
        image_add.setOnClickListener {
            startActivity(Intent(applicationContext, AddCityActivity::class.java))
        }
    }

    private fun requestWeather() {
        cityCode = sp.getString("cityCode", "")
        if ("" != cityCode) {
            doAsync {
                var url = "https://free-api.heweather.com/v5/weather?city=$cityCode&key=$key"
                var resultStr = URL(url).readText()
                defaultSharedPreferences
                        .edit()
                        .putString("weatherStr", resultStr)
                        .apply()
                uiThread { showWeather() }
            }
        }
    }
}
