package xbt.exp20.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xbt.exp20.db.City;
import xbt.exp20.db.County;
import xbt.exp20.db.Province;
import xbt.exp20.gson.Images;
import xbt.exp20.gson.Weather;

/**
 * 用于解析处理数据
 */

public class Utility {
    /**
     *解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){

        if(!TextUtils.isEmpty(response)){
            //如果字符序列不为空或长度为0
            try{
                JSONArray allProvinces = new JSONArray(response);
                for(int i = 0; i < allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId){

        if(!TextUtils.isEmpty(response)){
            //如果字符序列不为空或长度为0
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i = 0; i < allCities.length(); i++){
                    JSONObject CityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(CityObject.getString("name"));
                    city.setCityCode(CityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId){

        if(!TextUtils.isEmpty(response)){
            //如果字符序列不为空或长度为0
            try{
                JSONArray allCounties = new JSONArray(response);
                for(int i = 0; i < allCounties.length(); i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather类
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Images handleImagesrResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Images.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
