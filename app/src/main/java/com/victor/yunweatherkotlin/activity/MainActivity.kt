package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.victor.yunweatherkotlin.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find


/**
 * https://free-api.heweather.com/v5/weather?city=yourcity&key=yourkey
 *
 * key: ac4cceaa37be4d7099f04b2b0b456041
 *
 * such as : https://free-api.heweather.com/v5/weather?city=CN101191303&key=ac4cceaa37be4d7099f04b2b0b456041
 * */

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = find<TextView>(R.id.tv1)
        var i = 6
        bt.setOnClickListener {
            if (i < 50) {
                i++
                lottie_view.setAnimation("$i.json")
                lottie_view.progress = 0f
                lottie_view.playAnimation()
                tv.text = i.toString()
            }
        }
    }

}



