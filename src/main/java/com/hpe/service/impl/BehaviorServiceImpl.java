package com.hpe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpe.entity.Behavior;
import com.hpe.entity.count;
import com.hpe.mapper.BehaviorMapper;
import com.hpe.service.IBehaviorService;
import com.hpe.utils.StringTools;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zxj
 * @since 2019-04-17
 */
@Service
public class BehaviorServiceImpl extends ServiceImpl<BehaviorMapper, Behavior> implements IBehaviorService {

    @Autowired
    private BehaviorMapper behaviorMapper;

    @Override
    public int insertBehavior(Behavior behavior) throws RuntimeException {
        return behaviorMapper.insertBehavior(behavior);
    }

    @Override
    public List<Behavior> selectAllBehavior(String user) throws RuntimeException {
        return behaviorMapper.selectAllBehavior(user);
    }

    @Override
    public Map getEcharts(String user, String type) throws RuntimeException {
        List<count> list = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        list = behaviorMapper.getEcharts(user, type);

        if (list.size() > 0) {
            List<Integer> counts = new ArrayList<>();
            List<String> dates = new ArrayList<>();
            for (count c :
                    list) {
                counts.add(c.getCount());
                dates.add(c.getDate1());
            }
            map.put("date", dates);
            map.put("count", counts);
            return map;
        } else {
            return map;
        }
    }

    @Override
    public Map getEchartsNew(String user, String type, String sTime, String eTime) throws RuntimeException {
        List<count> list = new ArrayList();
        Map<String, Object> result = new HashMap<>();
        List<serie> se = new ArrayList<>();
        Map<String, Map<String, Integer>> map = new HashMap<>();
        list = behaviorMapper.getEchartsNew(user, type, sTime, eTime);
        if (list==null||list.size()==0){
            return result;
        }
        for (count c :
                list) {
            if (map.get(c.getName()) != null) {
                map.get(c.getName()).put(c.getDate1(), c.getCount());
            } else {
                HashMap<String, Integer> map1 = new HashMap<>();
                map1.put(c.getDate1(), c.getCount());
                map.put(c.getName(), map1);
            }
        }
        // 获取查询结果中不同的名字
        List<String> names = new ArrayList<>();

        for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
            String curTime = sTime;
            Map<String, Integer> curMap = entry.getValue();
            List<Integer> counts = new ArrayList();
            while (!curTime.equals(StringTools.getSpecifiedDayAfter(eTime))) {
                if (curMap.get(curTime) == null) {
                    counts.add(0);
                } else {
                    counts.add(curMap.get(curTime));
                }

                curTime = StringTools.getSpecifiedDayAfter(curTime);
            }
            names.add(entry.getKey());
            se.add(new serie(entry.getKey(), counts));
            // 获取到每一个人这七天的下载/上传数量
            result.put(entry.getKey(), counts);
        }
        result.put("name", names);
        result.put("se", se);
        result.put("date", StringTools.get7Days());
        return result;
    }



    @Data
    class serie{
        // 前台展示数据
//        name:sessionStorage.getItem('currentUser'),
//        type:'line',
//        stack: '总量',
//        areaStyle: {},
//        data:series
        private String name;
        private String type = "line";
        private String stack = "总量";
        private List<Integer> data;

        public serie(String name, List<Integer> list){
            this.name = name;
            this.data = list;
        }

    }
}
