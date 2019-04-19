package com.hpe.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hpe.entity.Behavior;
import com.hpe.service.IBehaviorService;
import com.hpe.service.IResourcesService;
import com.hpe.service.IUserService;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.entity.FileInfo;
import com.hpe.service.NetDiskService;
import com.hpe.utils.R;

@Controller
public class NetDiskController {
	//操作HDFS之前得先创建配置对象
	Configuration conf ;
	//创建操作HDFS的对象
	FileSystem fs ;

	public NetDiskController() {
		//初始化
		conf = new Configuration(true);
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private NetDiskService netDiskService;
	@Autowired
	private IResourcesService resourcesService;
	@Autowired
	private IBehaviorService behaviorService;
	@Autowired
	private IUserService userma;

	/**
	 * 测试
	 * @return
	 */
	@RequestMapping("demo")
	public String demo(){
		System.out.println("sdfasf");
		return "index.html";
	}
	/**
	 * 获取文件列表,POST请求方式
	 * 参数用字符串, 内容为绝对路径 例如 "/etc/hosts"
	 * @param path
	 * @return 返回一个json对象
	 * code 是状态码, 200为成功!
	 * fileList封装的是文件信息的list,前台可直接遍历
	 * {
    "		code": 200,
    "		fileList": [
        	{
	            "name": "aa",
	            "absolutePath": null,
	            "length": 0,
	            "isdir": true,
	            "block_replication": 0,
	            "blocksize": 0,
	            "modification_time": 1539352745590,
	            "access_time": 0,
	            "owner": "root",
	            "group": "supergroup"
        	},
        	{
        	....
        	}
        }
	 */
	@RequestMapping(value = "getFileList",method=RequestMethod.POST)
	@ResponseBody
	public R getFileList(String path){
		List<FileInfo> fileList = null;
		try {
			fileList = netDiskService.getFileList(path);
		} catch (FileNotFoundException e) {
			return R.error("文件不存在!");
		} catch (RuntimeException e) {

			return R.error("运行时异常");
		} catch (IOException e) {
			return R.error("读取失败!");
		}
		return R.ok("fileList",fileList);
	}
	/**
	 * 创建目录,也就是新建文件夹 ,POST请求
	 * 参数同 getFileList 字符串, 内容为绝对路径 例如 "/etc/hosts"
	 * @param path
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 */
	@RequestMapping(value = "createDir",method=RequestMethod.POST)
	@ResponseBody
	public R createDir(String path){
		R r;
		try {
			r = netDiskService.creatDir(path);
		} catch (IOException e) {
			return R.error("创建异常");
		}
		return r;
	}
	/**
	 * 上传 POST请求
	 * @param src:资源路径,本地
	 * dest 目标路径
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 */
	@RequestMapping(value = "uploadFileToHDFS",method=RequestMethod.POST)
	@ResponseBody
	public R uploadFileToHDFS(String src, String dest){
		try {//应先判断是否已存在
			netDiskService.copyFromLocalFile(src, dest);
		} catch (IOException e) {
			return R.error("上传失败!");
		}
		return R.ok("上传成功!");
	}
	/**
	 * 下载 POST请求
	 * @param src:资源路径,HDFS
	 * dest 目标路径,本地
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 */
	@RequestMapping(value = "downLoadFileFromHDFS",method=RequestMethod.POST)
	@ResponseBody
	public R downLoadFileFromHDFS(String src, String dest){
		Path srcPath = new Path(src);
		Path destPath = new Path(dest);
		try {//应先判断本地是否已存在
			fs.copyToLocalFile(false,srcPath, destPath,true);
		} catch (IOException e) {
			return R.error("下载失败!");
		}
		return R.ok("下载成功!");
	}
	/**
	 * 内部移动或复制  POST请求
	 * 注意此处两个都需要绝对路径
	 * @param
	 * 	bool:true 移动
	 * 	src:资源路径
	 * 	dest 目标路径
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 */
	@RequestMapping(value = "innerCopyOrMoveFile",method=RequestMethod.POST)
	@ResponseBody
	public R innerCopyOrMoveFile(boolean bool,String src, String dest, String user){
		Path srcPath = new Path(src);
		Path destPath = new Path(dest);
		int last = src.lastIndexOf("/");
		String is_exist = src.substring(last+1);
		Path is_exist_path = new Path(dest + is_exist);
		try {
			//应先判断是否已存在
			if(fs.exists(is_exist_path)){
				return R.error("文件已存在！");
			}
			FileUtil.copy(srcPath.getFileSystem(conf), srcPath, destPath.getFileSystem(conf), destPath,bool, conf);
			behaviorService.insertBehavior(new Behavior(userma.findUserByUserCode(user), src, dest + is_exist, "复制"));
		} catch (IOException e) {
			return R.error("失败!");
		}
		return R.ok("成功!");
	}
	/**
	 * 重命名  POST请求
	 * 注意此处两个都需要绝对路径
	 * @param
	 * 	src:资源路径
	 * 	dest 目标路径
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 */
	@RequestMapping(value = "renameFile",method=RequestMethod.POST)
	@ResponseBody
	public R renameFile(String src, String dest, String user){
		Path srcPath = new Path(src);
		Path destPath = new Path(dest);
		try {
			if (fs.exists(destPath)){
				// 新路径已存在
				return R.error("与其他文件名冲突！");
			}else {
				fs.rename(srcPath, destPath);
				behaviorService.insertBehavior(new Behavior(userma.findUserByUserCode(user), src, dest, "重命名"));
			}
		} catch (IOException e) {
			return R.error("重命名失败!");
		}
		return R.ok("重命名成功!");
	}
	/**
	 * 在HDFS创建文件  POST请求
	 * @param path 目标路径
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 */
	@RequestMapping(value = "createNewFile",method=RequestMethod.POST)
	@ResponseBody
	public R createNewFile(String path, String user){

		try {
			fs.createNewFile(new Path(path));
			behaviorService.insertBehavior(new Behavior(userma.findUserByUserCode(user), path, null, "添加"));

		} catch (IOException e) {
			return R.error("创建失败!");
		}
		return R.ok("创建成功!");
	}
	/**
     * 追加或重写内容  POST请求
     * @param filePath 目标路径 content内容 bool是否为重写: true 重写
     * @return 返回json对象
     *  code 封装了状态码
     *  msg 为返回信息,可以在前台展示
     */
    @RequestMapping(value = "/appendToHDFSFile",method=RequestMethod.POST)
    @ResponseBody //String filePath, String content,boolean bool
    public R appendToHDFSFile(@RequestParam Map<String,Object> params){
        String filePath = (String) params.get("filePath");
        String content = (String) params.get("content");
        String app =  (String) params.get("bool");
        Boolean bool = false;
        if(app.equals("追加")){
            bool = true;
        }else{//覆盖
            bool = false;
        }
        //System.out.println(filePath);
        FSDataOutputStream append =null;
        try {
            if(bool){//追加文件内容
                append = fs.append(new Path(filePath));
            }else{
                append = fs.create(new Path(filePath));
            }
            append.write(content.getBytes("UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
            return R.error("写入失败!");
        }finally {
            try {
                append.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                append.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.ok().put("code", 200);
    }
//	/**
//	 * 重写文件内容  POST请求
//	 * @param path 目标路径
//	 * @return 返回json对象
//	 * 	code 封装了状态码
//	 * 	msg 为返回信息,可以在前台展示
//	 */
//	@RequestMapping(value = "writeToHDFSFile",method=RequestMethod.POST)
//	@ResponseBody
//	public R writeToHDFSFile(String filePath, String content){
//		FSDataOutputStream append =null;
//		try {
//			append = fs.append(new Path(filePath));
//			append.write(content.getBytes("UTF-8"));
//
//		} catch (IOException e) {
//			return R.error("写入失败!");
//		}finally {
//			try {
//				append.flush();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				append.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return R.ok("写入成功!");
//	}
	/**
	 * 读文件内容  POST请求
	 * @param filePath 目标路径
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 * content 返回内容
	 */
	@RequestMapping(value = "/readFromHDFSFile",method=RequestMethod.POST)
	@ResponseBody
	public R readFromHDFSFile(@RequestParam("data_u") String filePath){
//		//传递过来的数据为data_u=%2F路径 %2F路径 %2F路径
//		//首先把data_u=去掉
//		filePath = filePath.substring(7);
//		//System.out.println(filePath);
//
//		//因为字符串乱码,所以需要把路劲中乱掉的/转回来
//		//1切割字符串,形成路径数组
//		String[] paths = filePath.split("%2F");
//		//2声明一个空字符串接受新路径
//		String path = "";
//		//3遍历数组,形成新路径
//		for(String p : paths){
//			path += "/" + p;
//		}
//		//4把开头多余的一个/删掉
//		path = path.substring(1);
//		//System.out.println(path);
		FSDataInputStream inputStream =null;
		StringBuffer content = new StringBuffer();
		String content2  = "";
		try {
			 inputStream = fs.open(new Path(filePath));

			FileStatus fileStatus = fs.getFileStatus(new Path(filePath));

			long len = fileStatus.getLen();

			byte[] b = new byte[1024];
			int read = inputStream.read(b);

			while(read != -1){
				content.append(new String(b));
				//System.out.println(new String(b));
				read = inputStream.read(b);
			}
			content2 = content.substring(0,(int) len);
			//System.out.println(content.toString());
		} catch (IOException e) {
			return R.error("读取失败!");
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return R.ok().put("content", content2);
	}
	/**
	 * 读文件内容  POST请求
	 * @param filePath 目标路径
	 * @return 返回json对象
	 * 	code 封装了状态码
	 * 	msg 为返回信息,可以在前台展示
	 * content 返回内容
	 */
	@RequestMapping(value = "delectFromHDFS",method=RequestMethod.POST)
	@ResponseBody
	public R delectFromHDFS(String path, String user){

		try {
			fs.delete(new Path(path),true);
			behaviorService.insertBehavior(new Behavior(userma.findUserByUserCode(user), path, null, "删除"));
		} catch (Exception e) {
			return R.error("删除失败!");
		}finally {
		}
		// 更新数据库表
		resourcesService.delResource(path);
		return R.ok("删除成功!");
	}



	@RequestMapping(value = "updateBehavior", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String updateBehav(String user){
		JSONObject json = new JSONObject();
		List<Behavior> list = null;
		try {
			list = behaviorService.selectAllBehavior(user);
		} catch (RuntimeException e) {
			json.put("msg", "获取失败!");
			return json.toString();
		}
		json.put("code", 200);
		json.put("content", list);
		return json.toString();
	}
}
