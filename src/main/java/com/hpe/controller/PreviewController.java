package com.hpe.controller;

import com.hpe.service.PreviewService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/preview")
public class PreviewController {

    @Autowired
    private PreviewService previewService;
    //操作HDFS之前得先创建配置对象
    Configuration conf;
    //创建操作HDFS的对象
    FileSystem fs;

    public PreviewController() {
        //初始化
        conf = new Configuration(true);
        try {
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/img")
    public void previewImg(HttpServletResponse response, String path) throws Exception {
        show(response, path, "image");
    }

    @RequestMapping("/music")
    public void previewMusic(HttpServletResponse response, String path) throws Exception {
        show(response, path, "audio");
    }

    @RequestMapping("/mov")
    public void previewMov(HttpServletResponse response, String path) throws Exception{
       show(response, path, "video");
    }

    @RequestMapping(value = "office", method = RequestMethod.GET)
    public void previewOffice(HttpServletResponse response,@Param("path") String path) throws Exception {
        if (path != null) {
            path = path.substring(path.lastIndexOf("/")+1);
            path = path.substring(0, path.lastIndexOf(".")) + ".pdf";
            String filePath = "D:\\IDEA workspace\\upload\\pdf\\" + path;
            File file = new File(filePath);
            byte[] data = null;
            try {
                FileInputStream input = new FileInputStream(file);
                data = new byte[input.available()];
                input.read(data);
                response.getOutputStream().write(data);
                input.close();
            } catch (Exception e) {
                System.out.print("pdf文件处理异常：" + e.getMessage());
            }
        }
    }


    /**
     * 响应文件
     * @param response
     * @param fileName  文件全路径
     * @param type  响应流类型
     */
    public void  show(HttpServletResponse response, String fileName,String type){
        FSDataInputStream inputStream = null;
        response.setContentType(type+"/*"); //设置返回的文件类型
        try{

            inputStream = fs.open(new Path(fileName));// 以byte流的方式打开文件
            int i = inputStream.available();
            byte[] buffer = new byte[1024];
            int read = inputStream.read(buffer);
            response.getOutputStream().write(buffer);
            while(read != -1){
                read = inputStream.read(buffer);
                response.getOutputStream().write(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                response.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 定时删除预览所产生的临时文件
     * 每天凌晨触发
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void myTimer(){
        try {
            File file=new File("D:\\IDEA workspace\\upload");		//获取临时文件存放的文件夹
            File[] files=file.listFiles();				//取文件夹下所有文件
            for(File f:files){					//遍历删除所有文件
                if (f.getName().equals("pdf")){
                    // PDF不删除
                }else {
                    delete(f);
                }
                System.out.println("当前时间是"+new Date().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将file文件夹下的所有文件清空
     * @param file
     */
    private boolean delete(File file){
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i=0; i<children.length; i++) {     //递归删除目录中的子目录下
                delete(new File(file, children[i]));
            }
        }
        // 目录此时为空，可以删除
        return file.delete();
    }



}
