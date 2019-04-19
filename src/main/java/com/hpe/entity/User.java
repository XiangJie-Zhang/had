package com.hpe.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zxj
 * @since 2019-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String salt;

    private String userName;

    private String passwd;

    private String email;

    private String realName;

    private String initLocation;

    private String birthday;

    private String roomSize;

    private String createTime;

    private String updateTime;


}
