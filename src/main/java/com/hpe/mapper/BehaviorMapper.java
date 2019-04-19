package com.hpe.mapper;

import com.hpe.entity.Behavior;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hpe.entity.count;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zxj
 * @since 2019-04-17
 */
public interface BehaviorMapper extends BaseMapper<Behavior> {

    int insertBehavior(Behavior behavior);

    @Select({"select * from behavior where name=#{user} order by date DESC limit 6"})
    List<Behavior> selectAllBehavior(@Param("user") String user);

    @Select({"SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\t( SELECT LEFT ( date, 10 ) AS date1, count( 1 ) as count,NAME  FROM behavior where name=#{user} and type=#{type} GROUP BY date1 ORDER BY date1 DESC LIMIT 3 ) a \n" +
            "ORDER BY\n" +
            "\tdate1;\n" +
            "\t"})
    List<count> getEcharts(@Param("user") String user, @Param("type") String type);


    List<count> getEchartsNew(@Param("user") String user, @Param("type") String type,@Param("sTime") String sTime, @Param("eTime") String eTime) throws RuntimeException;
}
