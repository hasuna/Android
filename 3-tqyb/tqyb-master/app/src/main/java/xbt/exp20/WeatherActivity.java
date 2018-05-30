package xbt.exp20;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xbt.exp20.gson.Forecast;
import xbt.exp20.gson.Images;
import xbt.exp20.gson.Weather;
import xbt.exp20.util.HttpUtil;
import xbt.exp20.util.Utility;

public class WeatherActivity extends AppCompatActivity {

    private NavigationView navigationView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView bingPicImg;

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    public DrawerLayout drawerLayout;

    public Button navbutton;

    private String picturePath;

    private String weatherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //使状态栏和背景图融合在一块
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//设置为全屏模式
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        //初始化各个控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navbutton = (Button) findViewById(R.id.nav_button);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weatherId", null);
        weatherId = getIntent().getStringExtra("weather_id");
        if (weatherId != null) {
            //选择过城市
            requestWeather(weatherId);
        }else {
            //刚打开应用的时候还没有选城市
            if(weatherString != null){
                //上一次打开应用的时候就选过城市
                Weather weather = Utility.handleWeatherResponse(weatherString);
                weatherId = weather.basic.weatherId;
                requestWeather(weatherId);
            }
        }

        //向下滑动刷新天气
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);//刷新进度条的颜色
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                if(weatherId != null) {
                    requestWeather(weatherId);
                }
            }
        });

        //背景图片

        String pictureString = prefs.getString("picturePath", null);
        if(pictureString != null){
            Glide.with(WeatherActivity.this).load(pictureString).into(bingPicImg);
        }else {
            loadBingPic();//请求今日必应背景图
        }



        //顶部按钮打开侧边栏
        navbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //侧边栏选项响应
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.select_city:
                        Intent intent = new Intent(WeatherActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.select_background:
                        //判断有没有使用相册的权限
                        if(ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(WeatherActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else{
                            openAlbum();
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 根据天气ID请求城市天气信息
     */
    public void requestWeather(final String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +"&key=0129b29ae876465f8d88b51c291f7b70";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);//将返回的json数据转换成weather对象
                //切换到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();//将数据缓存
                            editor.putString("weatherId",responseText);
                            editor.apply();
                            showWeatherInfo(weather);//显示内容
                            Toast.makeText(WeatherActivity.this, "天气获取成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(WeatherActivity.this, "天气获取失败"+ weatherId, Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);//传入false表示刷新事件结束
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "天气获取失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);//传入false表示刷新事件结束
                    }
                });
            }

        });
    }

    /**
     * 处理展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast: weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dataText = (TextView) view.findViewById(R.id.data_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数:" + weather.suggestion.carWash.info;
        String sport = "运动建议:" + weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
//        weatherLayout.setVisibility(View.VISIBLE);

    }

    private void loadBingPic(){
            String Url = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US";
            HttpUtil.sendOkHttpRequest(Url, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    final Images images = Utility.handleImagesrResponse(responseText);
                    final String url = "http://s.cn.bing.net"+ images.url;
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(WeatherActivity.this).load(url).into(bingPicImg);//加载图片
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "图片获取失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
        }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1 :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(WeatherActivity.this,"相册权限申请失败",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");// GET_CONTENT允许用户选择特殊种类的数据，并返回（特殊种类的数据：)
        intent.setType("image/*");//选择图片
        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    if (data == null){
                        Toast.makeText(WeatherActivity.this,"获取图片失败",Toast.LENGTH_SHORT).show();
                    }else{

                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            picturePath = cursor.getString(columnIndex);  //获取照片路径
                            cursor.close();
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("picturePath",picturePath);
                            editor.apply();
                            Glide.with(WeatherActivity.this).load(picturePath).into(bingPicImg);
//                            Glide.with(WeatherActivity.this).load(picturePath).into(navImg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

}

