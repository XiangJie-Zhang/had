package com.hpe.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zxj
 * @since 2019-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Dir implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer dirId;

    private String dirName;

    private String dirCreatetime;

    private String dirUser;

    private String dirHdfspath;

    private String dirUpdatetime;


}
