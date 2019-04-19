package com.hpe.controller;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpe.entity.Behavior;
import com.hpe.entity.Resources;
import com.hpe.entity.User;
import com.hpe.service.IBehaviorService;
import com.hpe.service.IResourcesService;
import com.hpe.service.IUserService;
import com.hpe.utils.HDFSUtils;
import com.hpe.utils.filetype.CheckExcelFileTypeUtil;
import com.hpe.utils.office.Office2PdfByJacob;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.service.NetDiskService;
import com.hpe.utils.R;

@Controller
public class UploadDownloadController {
	//操作HDFS之前得先创建配置对象
	Configuration conf ;
	//创建操作HDFS的对象
	FileSystem fs ;
	@Autowired
	private IResourcesService resourcesService;
	@Autowired
	private IBehaviorService behaviorService;
	@Autowired
	private IUserService userma;

	public UploadDownloadController() {
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
	@RequestMapping(value = "uploadFile")
	@ResponseBody
		public R uploadFile(HttpServletRequest request, String loginUser){
		 boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		 String path = request.getParameter("path");

		// 上传到本地的路径
		String inputSrc = "";

		if(!path.endsWith("/")){
			 path+="/";
		 }
			// 1.创建DiskFileItemFactory
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 2.创建
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				// 底层通过request获取数据，进行解析，将解析的数据封装到List<FileItem>
				List<FileItem> items = upload.parseRequest(request);
				// 3.遍历集合
				for (FileItem item : items) {
					if (item.isFormField()) {

						System.out.println(path);
					} else {
						String name = item.getName();
						// 不是表单组件 这就得到了<input type="file" name="f"> 这样的组件
						// 上传文件的名称
						name = name.substring(name.lastIndexOf("\\") + 1);
						InputStream in = null;
						try {
							// 上传到hdfs
							// 若果路径已经存在，则上传失败
							if (fs.exists(new Path(path+name))){
								System.out.println("文件已经存在");
								return R.error("文件已经存在");
							}else {
								Configuration conf = new Configuration();
								FSDataOutputStream out = fs.create(new Path(path+name));
								in = new BufferedInputStream(item.getInputStream());
								IOUtils.copyBytes(in, out, 4096, false);
								out.hsync();
								out.close();
								System.out.println("create file in hdfs:");
								FileStatus[] fileStatuses = fs.listStatus(new Path(path+name));
								for (FileStatus f :
										fileStatuses) {
									resourcesService.addResource(new Resources(f));
								}
								behaviorService.insertBehavior(new Behavior(userma.findUserByUserCode(loginUser), path+name, null, "上传"));
								// 上传到本地
								if (CheckExcelFileTypeUtil.checkFileType(name).equals("office")){
									inputSrc = "D:\\IDEA workspace\\upload\\" + loginUser + "\\" + name;
									File file = new File("D:\\IDEA workspace\\upload\\" + loginUser);
									if (!file.exists()) {file.mkdirs();}
									file = new File(inputSrc);
									if (file.exists()){file.delete();}
									if (name.contains("pdf")){
										// 如果本身就是pdf则没有需要转换
										item.write(new File("D:\\IDEA workspace\\upload\\pdf\\" + name));
									}else {
										item.write(file);

										// 启动新的线程来转换格式
										Office2PdfByJacob o = new Office2PdfByJacob(inputSrc, name);
										Thread thread = new Thread(o);
										thread.run();
									}
								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		System.out.println();
		return R.ok();
	}
	@RequestMapping(value = "downloadFile")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response, String user) throws IOException{

		 String path = request.getParameter("path");
		 if(path.endsWith("/")){
			 //目测是个目录
			 return;
		 }
		behaviorService.insertBehavior(new Behavior(userma.findUserByUserCode(user), path, null, "下载"));


		String fileName = path.substring(path.lastIndexOf("/")+1);
        // 3.设置content-disposition响应头控制浏览器弹出保存框，若没有此句则浏览器会直接打开并显示文件。中文名要经过URLEncoder.encode编码，否则虽然客户端能下载但显示的名字是乱码
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // 4.获取要下载的文件输入流
        InputStream in = fs.open(new Path(path));
        int len = 0;
        // 5.创建数据缓冲区
        byte[] buffer = new byte[1024];
        // 6.通过response对象获取OutputStream流
        OutputStream out = response.getOutputStream();
        // 7.将FileInputStream流写入到buffer缓冲区
        while ((len = in.read(buffer)) > 0) {
            // 8.使用OutputStream将缓冲区的数据输出到客户端浏览器
            out.write(buffer, 0, len);
        }
        in.close();
	}
}
