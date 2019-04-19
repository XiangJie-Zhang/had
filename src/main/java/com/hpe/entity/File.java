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
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer fileId;

    private String fileName;

    private String filePath;

    private Double fileSize;

    private String fileType;

    private Integer fileUser;

    private String fileUploadtime;

    private Integer fileDownnum;

    private Integer fileDir;


}
