package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
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
import org.jetbrains.anko.longToast
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
 *
 * bg: http://116.196.93.90/png/bg.png
 * */

class WeatherActivity : BaseActivity() {

    private val key = "ac4cceaa37be4d7099f04b2b0b456041"
    private lateinit var cityCode: String
    private lateinit var weatherStr: String
    private lateinit var sp: SharedPreferences
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        sp = defaultSharedPreferences
        handler = Handler()
        setLinstener()
        judgeIf()
        requestWeather()
//        bingPic()
        bgPic()
    }

    /**
     * 点返回按钮时，关闭drawer_layout
     * */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * SingleTask模式再次启动Activity时调用
     * */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.getBooleanExtra("update", false)!!) {
            swipe_refresh.isRefreshing = true
            requestWeather()
        }
    }

    /**bg背景图*/
    private fun bgPic(){
        Glide.with(applicationContext).load("http://116.196.93.90/png/bg1.png").into(iv_bing)
    }

    /**Bing背景图*/
    private fun bingPic() {
        doAsync {
            val imageUrl = URL("http://guolin.tech/api/bing_pic").readText()
            uiThread {
                Glide.with(applicationContext).load(imageUrl).into(iv_bing)
            }
        }
    }

    /**是否需要存入数据库和显示天气*/
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

    /**
     * 将从服务器查询到的天气展示出来
     * */
    private fun showWeather() {
        weatherStr = sp.getString("weatherStr", "")
        if (weatherStr == "") return
        else {
            can_gone.visibility = View.VISIBLE
            image_add.visibility = View.GONE
            val gson = Gson()
            val weather: Weather = gson.fromJson(weatherStr, Weather::class.java)
            val heWeather = weather.HeWeather5[0]
            //将城市信息和天气情况保存到数据库
            saveCounty(heWeather)
            title_city.text = heWeather.basic.city
            title_update_time.text = heWeather.basic.update.loc.split(" ")[1]+"发布"
            degree_text.text = heWeather.now.tmp + "℃"
            weather_info_text.text = heWeather.now.cond.txt
            aqi_text.text = heWeather.aqi?.city?.aqi
            pm25_text.text = heWeather.aqi?.city?.pm25
            comfort_title.text = "综合    "+heWeather.suggestion.comf.brf
            comfort_text.text =  heWeather.suggestion.comf.txt
            cw_title.text = "洗车    "+heWeather.suggestion.cw.brf
            cw_text.text =  heWeather.suggestion.cw.txt
            drsg_title.text = "穿衣    "+heWeather.suggestion.drsg.brf
            drsg_text.text =  heWeather.suggestion.comf.txt
            flu_title.text = "感冒    "+heWeather.suggestion.flu.brf
            flu_text.text =  heWeather.suggestion.flu.txt
            sport_title.text = "运动    "+heWeather.suggestion.sport.brf
            sport_text.text =  heWeather.suggestion.sport.txt
            trav_title.text = "旅游    "+heWeather.suggestion.trav.brf
            trav_text.text =  heWeather.suggestion.trav.txt
            uv_title.text = "紫外线    "+heWeather.suggestion.comf.brf
            uv_text.text =  heWeather.suggestion.comf.txt

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

    /**
     * 保存曾经查询过的城市名和天气信息，以便管理
     * */
    private fun saveCounty(weather: HeWeather) {
        val county = County(weather.basic.city, weather.now.cond.txt, sp.getString("cityCode", ""))
        val counties = DataSupport.findAll(County::class.java)
        var temp = ArrayList<String>()
        counties.mapTo(temp) { it.name }
        if (county.name in temp) return else county.save()
    }

    private fun setLinstener() {
        nav_button.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.edit_location -> {
                    startActivity(Intent(this, EditCitysActivity::class.java))
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        handler.postDelayed({
                            drawer_layout.closeDrawer(GravityCompat.START)
                        }, 100)
                    }
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        handler.postDelayed({
                            drawer_layout.closeDrawer(GravityCompat.START)
                        }, 100)
                    }
                    longToast("有隐藏内容待发现！")
                }
            }
            true
        }
        swipe_refresh.setOnRefreshListener {
            requestWeather()
//            bingPic()
            bgPic()
        }
        image_add.setOnClickListener {
            startActivity(Intent(applicationContext, AddCityActivity::class.java))
        }
    }

    /**请求天气*/
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
