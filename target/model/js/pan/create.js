/**
 * 新建文件文件夹js
 */
function dianji() {
	xvm.newFileShow=1;//新建文件
	
}
function dianji1() {
	xvm.newFileShow=1;//新建文件
}
function createFile(obj){
	var absolutePath = $(obj).attr("data-xpath");
	var fileName = document.getElementById("createFile").value;
	var path= absolutePath +fileName;
	$.ajax({
		url :"/had/createNewFile",//请求地址
		data:{
			path:path
		},
		type: "POST",//请求类型
		dataType: "json", //返回类型
		async: false ,//是否异步，默认为true
		success:function(data){
			if(data.msg == "创建成功!"){
				xvm.newFileShow=0;
				alert("创建成功!")
				getFileList();
			}else if(data.msg == "创建失败!"){
				alert("创建失败!")
			}else{
				alert("文件已存在!")	
			}
			}
	})
}
function cancel(){
	xvm.newFileShow=0;
}
