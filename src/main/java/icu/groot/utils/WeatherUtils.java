package icu.groot.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import icu.groot.pojo.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WeatherUtils {

    @Value("${info.alan.weather.administrativeCode}")
    private String administrativeCode;

    @Value("${info.alan.weather.ak}")
    private String ak;


    public Weather getWeather(){

        RestTemplate restTemplate = new RestTemplate();
        Map<String,String> map = new HashMap<String,String>();
        map.put("district_id",administrativeCode); // 地方行政代码
        map.put("data_type","all");//这个是数据类型
        map.put("ak",ak);//百度应用的AK
            String res = restTemplate.getForObject(
                "https://api.map.baidu.com/weather/v1/?district_id={district_id}&data_type={data_type}&ak={ak}",
                String.class,
                map);
        JSONObject json = JSONObject.parseObject(res);
        JSONObject result  = json.getJSONObject("result");
        JSONArray forecasts = result.getJSONArray("forecasts");
        List<Weather> weathers = forecasts.toJavaList(Weather.class);
        JSONObject now = result.getJSONObject("now");
        Weather weather = weathers.get(0);
        weather.setText_now(now.getString("text"));
        weather.setTemp(now.getString("temp"));
        weather.setWind_class(now.getString("wind_class"));
        weather.setWind_dir(now.getString("wind_dir"));

        JSONObject location = result.getJSONObject("location");
        weather.setAddress(location.getString("province") + " " + location.getString("city") + " " + location.getString("name") + "区");
        return weather;
    }
}