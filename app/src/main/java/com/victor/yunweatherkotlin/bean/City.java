package com.victor.yunweatherkotlin.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Victor on 2017/12/12.
 */
@Entity
public class City {
    private String cityCode;
    private String county;
    private String city;
    private String province;
    @Generated(hash = 989509160)
    public City(String cityCode, String county, String city, String province) {
        this.cityCode = cityCode;
        this.county = county;
        this.city = city;
        this.province = province;
    }
    @Generated(hash = 750791287)
    public City() {
    }
    public String getCityCode() {
        return this.cityCode;
    }
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    public String getCounty() {
        return this.county;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
}
