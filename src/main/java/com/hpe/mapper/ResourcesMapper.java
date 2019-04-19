package com.hpe.mapper;

import com.hpe.entity.Resources;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hpe.utils.datasource.DataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zxj
 * @since 2019-04-09
 */
public interface ResourcesMapper extends BaseMapper<Resources>{

    @DataSource("dataSource1")
    void insertMany1(List<Resources> resources) throws RuntimeException;

    @DataSource("dataSource1")
    int addResource(Resources resources) throws RuntimeException;

    @DataSource("dataSource1")
    @Update("truncate table resources")
    void truncateTable() throws RuntimeException;

    @DataSource("dataSource1")
    List<Resources> getTypeTimeline(@Param("user") String user,@Param("type") String type);

    @DataSource("dataSource1")
    @Delete({"delete from resources where resource_path = #{path}"})
    int delResource(@Param("path") String path);
}
