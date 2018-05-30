package xbt.exp20;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xbt.exp20.db.City;
import xbt.exp20.db.County;
import xbt.exp20.db.Province;
import xbt.exp20.util.HttpUtil;
import xbt.exp20.util.Utility;

/**
 * 用于遍历省市县数据的碎片
 */

public class ChooseAreaFragment extends android.support.v4.app.Fragment {

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private ProgressBar mProgress;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList  = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 选中的县
     */
    private County selectedCounty;

    /**
     * 当前被选中的级别
     */
    private int currentLevel;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);//获取控件实例
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        mProgress = (ProgressBar) view.findViewById(R.id.progressBar);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,dataList);//初始化适配器
        listView.setAdapter(adapter);
        return view;
    }

/*
 * 给listview和button设置点击事件
 */

    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //列表的点击事件响应
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //如果当前级别是省，那点击选取的就是某个省，而想要查询的是选中省有那些城市，市同理 ，想要查询这个城市有那些县
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();//查询全国所有的省，优先从数据库查询，如果没有查到再去服务器上查询
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();//查询全国所有的市，优先从数据库查询，如果没有查到再去服务器上查询
                }else if(currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        //返回按钮相应
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果当前级别是市，点击返回就是想重新选省，县同理，想重新选市
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();//查询全国所有的省，优先从数据库查询，如果没有查到再去服务器上查询
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();//查询全国所有的省，优先从数据库查询，如果没有查到再去服务器上查询
                }
            }
        });
        queryProvinces();//活动刚启动，没有选取省市县就直接展示全国34个省
    }

    /**
     * 开始加载省级数据
     * 查询全国所有的省，优先从数据库查询，如果没有查到再去服务器上查询
     */
    private void queryProvinces(){
        titleText.setText("中国");//头布局的标题设置为中国
        backButton.setVisibility(View.GONE);//隐藏返回按钮
        provinceList = DataSupport.findAll(Province.class);//从数据库中查询省级数据
        if(provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";//请求地址
            queryFromServer(address, "province");//从服务器上查询
        }
    }

    /**
     * 查询全国所有的市，优先从数据库查询，如果没有查到再去服务器上查询
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询全国所有的县，优先从数据库查询，如果没有查到再去服务器上查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县的数据
     */
    private void queryFromServer(String address, final String type){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            //向服务器发送请求
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread从子线程切回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            //用onResponse方法获得json天气数据
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);//handleProvinceResponse解析json天气数据
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if(result){
//                    mProgress.setVisibility(View.GONE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }
}
