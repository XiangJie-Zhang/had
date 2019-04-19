/**
 * 删除文件功能的js 
 */

function del(obj) {
	var absolutePath = $(obj).attr("data-url");
	
	
	if(confirm("确认删除吗？")){
		
				$.ajax({
					url : "/had/delectFromHDFS",// 请求地址
					type : "POST",// 请求类型
					datatype : "json",// 返回类型
					data : {
						path:absolutePath,
					},
					success : function(data) {
						if (data.code == 200) {
							getFileList();
						} else {
							alert("操作失败")
						}
					}
				})
	}
}