package com.victor.yunweatherkotlin.adapter

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.db.County

/**
 * Created by Victor on 2017/9/4.
 */
class QuickAdapter(data:List<County>) : BaseItemDraggableAdapter<County, BaseViewHolder>(R.layout.item_draggable_view,data){
    override fun convert(helper: BaseViewHolder?, item: County?) {
        helper?.setText(R.id.tv_city,item?.name)
        helper?.setText(R.id.tv_wea,item?.weaInfo)
    }
}