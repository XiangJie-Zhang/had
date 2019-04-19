package com.hpe.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


import com.hpe.utils.R;

public interface NetDiskService {

	public static final int  DIR_IS_EXIT = 2;
	public static final int  SUCCESS= 1;
	public static final int  FAILD= 0;
	List getFileList(String path)throws RuntimeException, FileNotFoundException, IOException;

	R creatDir(String path) throws IOException;

	R createOrUpdateDir (String oldDir, String newDir) throws IOException;

	void copyFromLocalFile(boolean b, String src, String dest) throws IOException;

	void copyFromLocalFile(String src, String dest) throws IOException;

	/**
	 * @Description:TODO描述： 查看文件内容
	 * @author: 赵化烙
	 * @date:   2018年10月13日 下午3:13:54
	 * @param src
	 * @throws IOException
	 */
	void viewFile(String src) throws IOException;
}
