package com.hpe.utils;

import com.hpe.entity.Resources;
import com.hpe.mapper.ResourcesMapper;
import com.hpe.utils.datasource.DataSource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


public class HDFSUtils {

    @Autowired
    private ResourcesMapper resourcesMapper;

    //操作HDFS之前得先创建配置对象
    Configuration conf;
    //创建操作HDFS的对象
    FileSystem fs;
    private static Logger logger = Logger.getLogger(HDFSUtils.class);

    public HDFSUtils() {
        //初始化
        conf = new Configuration(true);
        try {
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getResourcesType(String path) throws RuntimeException, FileNotFoundException, IOException {
        Path pathh = new Path(path);

        //获取当前路径下的文件列表
        FileStatus[] fileStatuses = fs.listStatus(pathh);
        ArrayList<Resources> resources = new ArrayList<>();

        for (FileStatus f :
                fileStatuses) {
            if (!f.isDirectory()) {
                Resources res = new Resources(f);
                resources.add(res);
            } else {
                String s = f.getPath().toString();
                getResourcesType(s);
            }
        }
        if (resources != null && resources.size() > 0) {
            resourcesMapper.insertMany1(resources);
        }
    }

    @DataSource("dataSource1")
    public void init() {
        resourcesMapper.truncateTable();
        try {
            getResourcesType("/");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String salt() {
        Random ranGen = new SecureRandom();
        byte[] aesKey = new byte[20];
        ranGen.nextBytes(aesKey);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < aesKey.length; i++) {
            String hex = Integer.toHexString(0xff & aesKey[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public static String setSize(double size) {
        //获取到的size为：1705230
        int GB = 1024 * 1024 * 1024;//定义GB的计算常量
        int MB = 1024 * 1024;//定义MB的计算常量
        int KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (double) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (double) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (double) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return String.valueOf(resultSize);

    }




}
