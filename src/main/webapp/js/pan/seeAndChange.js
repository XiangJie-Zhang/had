/**
 * 查看文件及修改文件
 */

var message; //用于显示内容
var vm;    //Vue
var show;  //控制隐藏域

$(function(){
	vm = new Vue({
		el: '#seeAndEdit',
		data: {
			show:true,  //控制隐藏域
		    message: ""
		}
	})
})

/**
 * 查看文件内容
 * @param data_url
 */
function viewFile(data_url){
	var data_u = data_url.getAttribute("data-url");
	$.ajax({
		url : "/had/readFromHDFSFile",// 请求地址
		type : "POST",// 请求类型
		datatype : "json",// 返回类型
		contentType :"application/json",//json字符串
		data :{
			data_u:data_u
		},
		success : function(data) {
			content = data.content;
			alert(data.content);

		}
	})
}

/**
 * 追加文件内容
 * @param data_url
 */
function appendFile(data_url){
	var data_u = data_url.getAttribute("data-url");
	
	//查看该文件内容
	$.ajax({
		url : "/had/readFromHDFSFile",// 请求地址
		type : "POST",// 请求类型
		datatype : "json",// 返回类型
		contentType :"application/json",//json字符串
		data :{
			data_u:data_u
		},
		success : function(data) {
			//获得追加内容
			var append = prompt(data.content,"请输入要追加的内容:");
			
			$.ajax({
				url : "/had/appendToHDFSFile",// 请求地址
				type : "POST",// 请求类型
				datatype : "json",// 返回类型
				data :{
					filePath:data_u, //hdfs上面的路径
					content:append,  //要追加的内容 
					bool:"追加"
				},
				success : function(data) {
					if(data.code == 200){
						alert("追加成功!!!");
					}
				}
			})
		}
	})
	
}


/**
 * 进入編輯文件内界面
 * @param data_url
 */
var data_u;
function editFile(data_url){
	
	//查看文件内容,显示在文本框
	data_u = data_url.getAttribute("data-url");
	$.ajax({
		url : "/had/readFromHDFSFile",// 请求地址
		type : "POST",// 请求类型
		datatype : "json",// 返回类型
		contentType :"application/json",//json字符串
		data :{
			data_u:data_u
		},
		success : function(data) {
			message = data.content;
			vm.message = $.trim(message);
			vm.show = false;
		}
	})
	
	
	
}

//提交编辑后的内容
$("#editButton").click(
		
	function(){
		//编辑完,点提交
		$.ajax({
			url : "/had/appendToHDFSFile",// 请求地址
			type : "POST",// 请求类型
			datatype : "json",// 返回类型
			data :{
				filePath:data_u, //hdfs上面的路径
				content:vm.message,  //要追加的内容 
				bool:"覆盖"
			},
			success : function(data) {
				if(data.code == 200){
					alert("编辑成功!!!");
					vm.show = true;
				}
			}
		})
	}
);
