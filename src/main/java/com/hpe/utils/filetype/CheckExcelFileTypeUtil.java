package com.hpe.utils.filetype;

/**
 * @author guoxk
 * @version 创建时间 2016年7月17日 上午10:47:26
 * <p>
 * 类描述：获取文件类型
 */
public class CheckExcelFileTypeUtil {
    public static String checkFileType(String fileName) {
        if (fileName == null) {
            fileName = "文件名为空！";
            return fileName;


        } else {
// 获取文件后缀名并转化为写，用于后续比较
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
// 创建图片类型数组
            String img[] = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
                    "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
            for (int i = 0; i < img.length; i++) {
                if (img[i].equals(fileType)) {
                    return "picture";
                }
            }


// 创建文档类型数组
            String document[] = {"doc", "docx", "xls", "xlsx", "wps", "pdf","pptx", "dps","et","ppt"};
            for (int i = 0; i < document.length; i++) {
                if (document[i].equals(fileType)) {
                    return "office";
                }
            }
// 创建视频类型数组
            String video[] = {"mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm"};
            for (int i = 0; i < video.length; i++) {
                if (video[i].equals(fileType)) {
                    return "mov";
                }
            }
// 创建音乐类型数组
            String music[] = {"mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg",
                    "m4a", "vqf"};
            for (int i = 0; i < music.length; i++) {
                if (music[i].equals(fileType)) {
                    return "music";
                }
            }


        }
        return "none";
    }
    public static String getOfficeType(String fileName){
        if (fileName == null) {
            fileName = "文件名为空！";
            return fileName;


        } else {
// 获取文件后缀名并转化为写，用于后续比较
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
// 创建图片类型数组
            String img[] = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
                    "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
            for (int i = 0; i < img.length; i++) {
                if (img[i].equals(fileType)) {
                    return "picture";
                }
            }


// ppt
            String document[] = {"dps", "pptx", "ppt"};
            for (int i = 0; i < document.length; i++) {
                if (document[i].equals(fileType)) {
                    return "ppt";
                }
            }
            // word
            String word[] = {"docx", "doc", "wps"};
            for (int i = 0; i < word.length; i++) {
                if (word[i].equals(fileType)) {
                    return "word";
                }
            }
            // xls
            String xls[] = {"xls", "xlsx", "et"};
            for (int i = 0; i < xls.length; i++) {
                if (xls[i].equals(fileType)) {
                    return "excel";
                }
            }
// 创建视频类型数组
            String video[] = {"mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm"};
            for (int i = 0; i < video.length; i++) {
                if (video[i].equals(fileType)) {
                    return "mov";
                }
            }
// 创建音乐类型数组
            String music[] = {"mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg",
                    "m4a", "vqf"};
            for (int i = 0; i < music.length; i++) {
                if (music[i].equals(fileType)) {
                    return "music";
                }
            }


        }
        return "none";
    }
}
