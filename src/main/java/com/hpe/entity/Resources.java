package com.hpe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.hpe.utils.HDFSUtils;
import com.hpe.utils.StringTools;
import com.hpe.utils.filetype.CheckExcelFileTypeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.hadoop.fs.FileStatus;

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
public class Resources implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "resource_id", type = IdType.AUTO)
    private String resourceId;

    private String resourceName;

    private String resourcePath;

    private Double size;

    private String type;

    private String owner;

    private Integer isDelete;

    /**
     * 修改时间
     */
    private String modification_time;

    public Resources(){}

    public Resources(FileStatus fileStatus){
        this.resourceId = UUID.randomUUID().toString();
        this.isDelete = 0;
        this.size = Double.valueOf(fileStatus.getLen());
        this.resourcePath = StringTools.changePath(fileStatus.getPath().toString());
        this.resourceName = fileStatus.getPath().getName();
        if (fileStatus.getPath().getName().contains(".")){
            this.type = CheckExcelFileTypeUtil.checkFileType(fileStatus.getPath().getName());
        }else {
            this.type = "none";
        }
        String tempPath = this.resourcePath.substring(1);
        if (tempPath.contains("/")){
            this.owner = tempPath.substring(0,tempPath.indexOf("/"));
        }else if (resourcePath.equals("/"+tempPath)){
            this.owner = "admin";
        } else {
            this.owner = tempPath;
        }
        Date date = new Date(fileStatus.getModificationTime());
        this.modification_time = StringTools.getCurrentTime(date);
    }
}
