package com.hpe.service.impl;

import com.hpe.service.PreviewService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class PreviewServiceImpl implements PreviewService {
    //操作HDFS之前得先创建配置对象
    Configuration conf ;
    //创建操作HDFS的对象
    FileSystem fs ;

    public PreviewServiceImpl() {
        //初始化
        conf = new Configuration(true);
        try {
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public byte[] perviewImg(String abPath) throws Exception {
        return new byte[0];
    }
}
