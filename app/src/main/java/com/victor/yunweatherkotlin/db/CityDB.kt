package com.victor.yunweatherkotlin.db

import org.litepal.crud.DataSupport



data class CityDB(var cityCode : String, var county : String, var city : String, var province : String) : DataSupport()

