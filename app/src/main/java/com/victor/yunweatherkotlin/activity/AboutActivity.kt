package com.victor.yunweatherkotlin.activity

import android.os.Bundle
import android.util.Log
import com.airbnb.lottie.LottieAnimationView
import com.victor.yunweatherkotlin.R
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

/**
 * jd服务器：http://116.196.93.90/Json/1.json
 * */
class AboutActivity : BaseActivity() {

    var judge = true
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val lottie = find<LottieAnimationView>(R.id.lottie_1)
        setListener(lottie)

    }

    private fun setListener(lottie: LottieAnimationView) {
        iv_back_about.setOnClickListener { finish() }
        tv_yun.setOnClickListener {
            if (judge) {
                longToast("恭喜你发现新大陆！")
                judge = false
            }
            var jsonString: String
            var jsonIn : InputStream
            if (i < 50) {
                i++
                doAsync {
                    jsonIn = assets.open("$i.json")
                    val list = jsonIn.reader().readLines()
                    Log.i("hahaha",list.size.toString())
                    val sb = StringBuilder()
                    for (s in list){
                        sb.append(s)
                    }
                    jsonString = sb.toString()
                    print(jsonString)
//                    jsonString = URL("http://116.196.93.90/Json/$i.json").readText()
                    val json = JSONObject(jsonString)
                    uiThread {
                        lottie.setAnimation(json)
                        lottie.progress = 0f
                        lottie.playAnimation()
                    }
                }
            }

        }
    }



}
