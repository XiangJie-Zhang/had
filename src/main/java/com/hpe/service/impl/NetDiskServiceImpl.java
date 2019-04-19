package com.hpe.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import com.hpe.entity.FileInfo;
import com.hpe.entity.FilesSort;
import com.hpe.service.NetDiskService;
import com.hpe.utils.R;
@Service
public class NetDiskServiceImpl implements NetDiskService {
	//操作HDFS之前得先创建配置对象
	Configuration conf ;
	//创建操作HDFS的对象
	FileSystem fs ;

	public NetDiskServiceImpl() {
		//初始化
		conf = new Configuration(true);
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List getFileList(String path) throws RuntimeException, FileNotFoundException, IOException {

		Path ppath = new Path(path);
		FileStatus[] listStatus = null;

		//获取当前路径下的文件列表
		listStatus = fs.listStatus(ppath);

		//封装成list
		List<FileInfo> list = new ArrayList<>();
		for (FileStatus fileStatus : listStatus) {

			FileInfo fileInfo = new FileInfo(fileStatus);
			list.add(fileInfo);
		}
		list.sort(new FilesSort());
		//返回
		return list;
	}

	@Override
	public R creatDir(String dir) throws IOException {
		Path path = new Path(dir);
		//查看目录是否存在
		if(fs.exists(path)){
			String name =dir.substring(dir.lastIndexOf("/"));
			return R.error(name+"已经存在");
		}
		//创建目录
		if(fs.mkdirs(path)){
			return R.ok("创建成功!");
		}
		//创建失败
		return R.error("创建失败!");
	}

	@Override
	public void copyFromLocalFile(String src, String dest) throws IOException {

		Path srcPath = new Path(src);
		Path destPath = new Path(dest);
		fs.copyFromLocalFile(srcPath, destPath);
	}
	@Override
	public void copyFromLocalFile(boolean bool, String src, String dest) throws IOException {

		Path srcPath = new Path(src);
		Path destPath = new Path(dest);
		fs.copyFromLocalFile(bool,srcPath, destPath);
	}

	@Override
	public void viewFile(String src) throws IOException {


	}

	public R createOrUpdateDir(String oldDir, String newDir) throws IOException {
		Path oldPath = new Path("/" + oldDir);
		Path newPath = new Path("/" + newDir);
		// 判断目录是否存在
		if(fs.exists(oldPath)){
			if (newDir.isEmpty()){
				return R.ok();
			}
			// 如果新老目录一致则不修改
			if (oldDir.equals(newDir)){
				return R.ok("创建成功");
			}else {
				// 不一致则修改目录名
				fs.rename(oldPath, newPath);
			}
		}
		//创建目录
		if(fs.mkdirs(oldPath)){
			return R.ok("创建成功!");
		}
		//创建失败
		return R.error("创建失败!");
	}
}
