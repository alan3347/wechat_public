package icu.groot.controller;

import icu.groot.pojo.Weather;
import icu.groot.utils.CaiHongPiUtils;
import icu.groot.utils.JiNianRiUtils;
import icu.groot.utils.WeatherUtils;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @JavaVersion : java version 1.8+
 * @Author : Alan
 * @ClassName : Pusher
 * @Version : 1.0.0
 * @Date : 2022/8/25 17:47
 * @Description :
 */

@Component
public class Pusher {


//    private String appId = "wxb62b608c140641d4";
//    private String secret = "eddb4b20d4e6b9a11a21f83c5ee5420d";

//    private String userId = "o6N9c5oQ6Cit77LKWaHTK4v2L5GQ";
//    private String modelId = "Ddo3ijJ9yk27eurf56PGRH5EbHyDLLbSNDMaH0WXyVU";


    @Value("${info.alan.weChat.appId}")
    private String appId;
    @Value("${info.alan.weChat.secret}")
    private String secret;

    @Value("${info.alan.weChat.userId}")
    private String userId;
    @Value("${info.alan.weChat.modelId}")
    private String modelId;

    @Value("${info.alan.note}")
    private String note;

    @Autowired
    WeatherUtils weatherUtils;

    @Autowired
    JiNianRiUtils jiNianRiUtils;

    @Autowired
    CaiHongPiUtils caiHongPiUtils;


//    public static void main(String[] args) {
////        System.out.println(note);
//        new Pusher().push();
//
//    }

//    public void show(){
//        System.out.println(note);
//    }


    public void push(){
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appId);
        wxStorage.setSecret(secret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(userId)//用户id
                .templateId(modelId)//模板id
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
//        Weather weather = WeatherUtils.getWeather();
        Weather weather = weatherUtils.getWeather();
        Map<String, String> map = caiHongPiUtils.getEnsentence();
        templateMessage.addData(new WxMpTemplateData("riqi",weather.getDate() + "  "+ weather.getWeek(),"#00BFFF"));//日期
        templateMessage.addData(new WxMpTemplateData("chegnshi",weather.getAddress(),"#2B2B2B"));//城市
        templateMessage.addData(new WxMpTemplateData("tianqi",weather.getText_now(),"#00FFFF"));//天气
        templateMessage.addData(new WxMpTemplateData("low",weather.getLow() + "","#173177"));//最低温度
        templateMessage.addData(new WxMpTemplateData("temp",weather.getTemp() + "","#EE212D"));//当前温度
        templateMessage.addData(new WxMpTemplateData("high",weather.getHigh()+ "","#FF6347" ));//最高温度
        templateMessage.addData(new WxMpTemplateData("windclass",weather.getWind_class()+ "","#42B857" ));//风级
        templateMessage.addData(new WxMpTemplateData("winddir",weather.getWind_dir()+ "","#B95EA3" ));//风向
        templateMessage.addData(new WxMpTemplateData("lianai",jiNianRiUtils.getLianAi()+"","#FF1493"));//恋爱日期
        templateMessage.addData(new WxMpTemplateData("caihongpi",caiHongPiUtils.getCaiHongPi(),"#FF69B4"));//彩虹屁
        templateMessage.addData(new WxMpTemplateData("shengriTa",jiNianRiUtils.getBirthday_Ta()+"","#FFA500"));//ta生日
        templateMessage.addData(new WxMpTemplateData("shengriMe",jiNianRiUtils.getBirthday_Me()+"","#FFA500"));//wo生日
        templateMessage.addData(new WxMpTemplateData("en",map.get("en") +"","#C71585"));//英文
        templateMessage.addData(new WxMpTemplateData("zh",map.get("zh") +"","#C71585"));//中文
//        String beizhu = "宝❤宝";
        if(jiNianRiUtils.getLianAi() % 365 == 0){
            note = "今天是恋爱" + (jiNianRiUtils.getLianAi() / 365) + "周年纪念日！";
        }
        if(jiNianRiUtils.getBirthday_Ta()  == 0){
            note = "今天是生日，生日快乐呀！";
        }
        if(jiNianRiUtils.getBirthday_Me()  == 0){
            note = "今天是生日，生日快乐呀！";
        }
        templateMessage.addData(new WxMpTemplateData("beizhu",note,"#FF0000"));

        try {
            System.out.println(templateMessage.toJson());
            System.out.println(wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage));
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
