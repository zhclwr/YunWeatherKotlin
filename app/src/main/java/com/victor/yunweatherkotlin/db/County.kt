package com.victor.yunweatherkotlin.db

import org.litepal.crud.DataSupport

/**
 * Created by Victor on 2017/9/4.
 */
data class County(var name:String, var weaInfo:String, var cityCode:String) :DataSupport()