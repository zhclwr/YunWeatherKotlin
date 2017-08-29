package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.os.Bundle
import com.victor.yunweatherkotlin.R
import kotlinx.android.synthetic.main.activity_edit_citys.*

class EditCitysActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_citys)
        setListener()
    }

    private fun setListener() {
        iv_back.setOnClickListener { finish() }
        iv_add.setOnClickListener { startActivity(Intent(this, AddCityActivity::class.java)) }
    }
}
