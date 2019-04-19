package com.hpe.service.impl;

import com.hpe.entity.Resources;
import com.hpe.entity.resourceVo;
import com.hpe.mapper.ResourcesMapper;
import com.hpe.service.IResourcesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxj
 * @since 2019-04-09
 */
@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources> implements IResourcesService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Override
    public Map<String, List<Resources>> getTypeTimeline(String user, String type) throws RuntimeException {
        List<Resources> timeline = resourcesMapper.getTypeTimeline(user, type);
        Map<String, List<Resources>> map = new HashMap<>();
        if (timeline!=null && timeline.size()>0){
            for (Resources r:
                    timeline) {
                if (map.get(r.getModification_time().split(" ")[0].replaceAll("-", "")) == null){
                    List<Resources> res = new ArrayList<>();
                    res.add(r);
                    map.put(r.getModification_time().split(" ")[0].replaceAll("-", ""), res);
                }  else{
                    map.get(r.getModification_time().split(" ")[0].replaceAll("-", "")).add(r);
                }
            }
        }

        return map;
    }

    @Override
    public int addResource(Resources resources) throws RuntimeException {
        return resourcesMapper.addResource(resources);
    }

    @Override
    public int delResource(String path) throws RuntimeException {
        return resourcesMapper.delResource(path);
    }
}
