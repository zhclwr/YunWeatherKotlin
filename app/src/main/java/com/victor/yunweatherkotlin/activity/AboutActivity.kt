package com.victor.yunweatherkotlin.activity

import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.victor.yunweatherkotlin.R
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.*
import org.json.JSONObject
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
                longToast("   恭喜你发现新大陆！" +
                        "\n流量网络请勿多次点击")
                judge = false
            }
            if (i < 50) {
                i++
                doAsync {
                    var jsonString = URL("http://116.196.93.90/Json/$i.json").readText()
                    var json = JSONObject(jsonString)
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
