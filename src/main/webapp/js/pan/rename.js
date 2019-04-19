/**
 * 重命名方法的js
 */
	var rname;
	var dirs;
	var absolutePath;
	var id ;
	var xpath;
	$(function() {
		rname = new Vue({
			el:"#table1",
			data:{
				list:[],
				path:"/",
				show:true
			}
		})
	})

	function rename(obj) {
		absolutePath = $(obj).attr("data-url");
		if(confirm("确认重命名吗？")){
			for (var int = 0; int < xvm.list.length; int++) {
				var path = xvm.list[int];
				if(absolutePath==path.absolutePath){
					xvm.list[int].rename=true;
					id = xvm.list[int].name;
				}
			}
			
		}
	}
	
	function re(){
		var p = $("input[id='"+id+"']").val();
		var rpath = xvm.xpath+p;
		$.ajax({
			
			url : "/had/renameFile",// 请求地址
			type : "POST",// 请求类型
			datatype : "json",// 返回类型
			data :{
				src:absolutePath,
				dest:rpath
				},
			success : function(data) {
					if (data.code == 200) {
					alert("操作成功")// vm.query()调用其他的方法
					getFileList();
				} else {
					alert("操作失败")
				}
			}
		})
	}