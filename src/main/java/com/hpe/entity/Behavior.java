package com.hpe.entity;

import java.io.Serializable;
import java.util.Date;

import com.hpe.utils.StringTools;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zxj
 * @since 2019-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Behavior implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String oldpath;

    private String newpath;

    private String name;

    private String date;

    private String type;

    private String msg;

    public Behavior(){

    }

    public Behavior(User user, String oldpath, String newPath, String type){
        this.id = user.getUserId();
        this.oldpath = oldpath;
        this.newpath = newPath;
        this.name = user.getUserName();
        this.date = StringTools.getCurrentTime(new Date());
        this.type = type;
        this.msg = theMsg();
    }

    public String theMsg(){
        String  result = "";
        if ("删除".equals(this.type) || "上传".equals(this.type) || "下载".equals(this.type) || "添加".equals(this.type)){
            result = "用户"+ this.name+ "于"+ this.date+ this.type + "了文件" +this.oldpath;
        }else if ("重命名".equals(this.type)){
            result = "用户"+ this.name+ "于"+ this.date+ this.type + "了文件" +this.oldpath + "为 " + newpath;
        }else {
            result = "用户"+ this.name+ "于"+ this.date+ this.type + "了文件" +this.oldpath + "到 " + newpath;

        }
        return result;
    }

    @Override
    public String toString() {
        return "Behavior{" +
                "id='" + id + '\'' +
                ", oldpath='" + oldpath + '\'' +
                ", newpath='" + newpath + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
