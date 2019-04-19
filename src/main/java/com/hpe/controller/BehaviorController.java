package com.hpe.controller;


import com.alibaba.fastjson.JSONObject;
import com.hpe.service.IBehaviorService;
import com.hpe.utils.StringTools;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zxj
 * @since 2019-04-17
 */
@Controller
@RequestMapping("/behavior")
public class BehaviorController {
    @Autowired
    private IBehaviorService behaviorService;

    @RequestMapping(value = "echarts", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getEcharts(String user, String type){
        JSONObject json = new JSONObject();
        if (user.isEmpty() || type.isEmpty()){
            json.put("msg", "参数为空");
        }

        Map map = behaviorService.getEcharts(user, type);
        if (map.size()>0){
            json.put("content", map);
            json.put("code", 200);
        }else {
            json.put("msg", "查询结果不存在!");
        }
        return json.toString();
    }


    @RequestMapping(value = "echartsNew", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getEchartsNew(String user, String type){
        JSONObject json = new JSONObject();
        if (user.isEmpty() || type.isEmpty()){
            json.put("msg", "参数为空");
        }

        Map map = behaviorService.getEchartsNew(user, type, StringTools.get7DaysAgo(), StringTools.getSpecifiedDayBefore());
        if (map.size()>0){
            json.put("content", map);
            json.put("code", 200);
        }else {
            json.put("msg", "查询结果不存在!");
        }
        return json.toString();
    }


}
