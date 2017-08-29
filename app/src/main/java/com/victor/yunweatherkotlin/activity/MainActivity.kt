package com.victor.yunweatherkotlin.activity

import android.os.Bundle
import com.victor.yunweatherkotlin.R


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
    }
}



