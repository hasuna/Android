package xbt.exp20.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//引用五个实体类
public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;//forecast包含是一个数组，因此使用list集合来引用
}
