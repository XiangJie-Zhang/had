package com.hpe.service;

public interface PreviewService {
    /*
     * 获取图片二进制
     * @param abPath
     * @return
     */
    byte[] perviewImg(String abPath) throws Exception;
}
