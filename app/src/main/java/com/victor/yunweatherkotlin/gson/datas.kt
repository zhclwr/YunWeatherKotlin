package com.victor.yunweatherkotlin.gson


data class AQI(var city: City)
/**aqi:空气质量指数，pm25:pm25指数*/
data class City(var aqi: String, var pm25: String?)

/** city：城市名 update：封装更新时间*/
data class Basic(var city: String, var update: Update)

/**loc:当地时间（api文档是这么写的，但是比现实晚19分钟*/
data class Update(var loc: String)

/**data:日期 tem:封装温度 cond:封装天气信息*/
data class Forecast(var date: String, var tmp: Tem, var cond: Cond)

/**max:最高温度 min:最低温度*/
data class Tem(var max: String, var min: String)

/**txt_d:具体天气情况*/
data class Cond(var txt_d: String)

/**此刻的天气tem:此刻的温度 cond:封装具体天气信息*/
data class Now(var tmp: String, var cond: CondNow)

/**txt:具体天气信息*/
data class CondNow(var txt: String)

data class Suggestion(var comf: Comfort, var cw: CarWash, var sport: Sport, var drsg: Drsg, var air: Air, var flu: Flu, var trav: Trav, var uv: Uv)
data class Comfort(var brf: String, var txt: String)
data class CarWash(var brf: String, var txt: String)
data class Sport(var brf: String, var txt: String)
data class Drsg(var brf: String, var txt: String)
data class Air(var brf: String, var txt: String)
data class Flu(var brf: String, var txt: String)
data class Trav(var brf: String, var txt: String)
data class Uv(var brf: String, var txt: String)
data class HeWeather(var status: String, var basic: Basic, var aqi: AQI?, var now: Now,
                     var suggestion: Suggestion, var daily_forecast: List<Forecast>)

data class Weather(var HeWeather5: List<HeWeather>)
