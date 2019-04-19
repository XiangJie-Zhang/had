package com.hpe.controller;


import com.alibaba.fastjson.JSONObject;
import com.hpe.entity.Resources;
import com.hpe.entity.User;
import com.hpe.entity.resourceVo;
import com.hpe.service.IResourcesService;
import com.hpe.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zxj
 * @since 2019-04-09
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {
    @Autowired
    private IResourcesService resourcesService;
    @Autowired
    private IUserService userService;


    /**
     * 获取user用户拥有的某个类型的集合
     * @param user
     * @param type
     * @return
     */
    @RequestMapping(value = "timeline", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getTypeTimeline(@RequestParam("user") String user, @RequestParam("type")String type){
        JSONObject json = new JSONObject();
        if (user.isEmpty()){
            json.put("msg", "用户名为空");
            return json.toString();
        }else if (type.isEmpty()){
            json.put("msg", "未输入类型");
            return json.toString();
        }
        /**
         * 找到用户名为user的用户的初始化路径
         */
        User code = userService.findUserByUserCode(user);


        /**
         * 根据路径/xxx得到资源的拥有者xxx，获取它的资源
         */
        if ("admin".equals(user)){
            Map<String, List<Resources>> timeline = resourcesService.getTypeTimeline("admin", type);
            json.put("data", timeline);
            json.put("code", 200);
        }else if (code!=null){
            Map<String, List<Resources>> timeline = resourcesService.getTypeTimeline(code.getInitLocation().substring(1), type);
            json.put("data", timeline);
            json.put("code", 200);
        }else {
            json.put("msg", "不存在此用户或者此用户下没有" + type + "文件");
            return json.toString();
        }


        return json.toString();
    }
}
