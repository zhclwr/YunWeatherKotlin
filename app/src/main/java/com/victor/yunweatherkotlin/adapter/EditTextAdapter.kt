package com.victor.yunweatherkotlin.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.bean.City
import com.victor.yunweatherkotlin.db.CityDB

/**
 * Created by Administrator on 2017/9/6.
 */
class EditTextAdapter(data:MutableList<City>): BaseQuickAdapter<City,BaseViewHolder>(R.layout.item,data) {
    override fun convert(p0: BaseViewHolder?, p1: City?) {
        p0?.setText(R.id.tv1_item,p1?.county)
                ?.setText(R.id.tv2_item,p1?.city)
                ?.setText(R.id.tv3_item,p1?.province)
    }

  //泗


}