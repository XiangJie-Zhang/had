function sss(){
	alert("fd");
	$(".ui-input-file").val("");
	$("#upload").submit();
}
function test(){
	var path=xvm.xpath;
	var loginUser = sessionStorage.getItem("currentUser");
	var form = document.getElementById("upload");
	var files = document.getElementById("fileupload");
	  oData = new FormData();
	  oData.append('file', files.files[0], files.files[0].name);

	  $.ajax({
		  url: "uploadFile?path="+path+"&loginUser="+loginUser,
		  type: "POST",
		  data: oData,
		  processData: false,  // 不处理数据
		  contentType: false,   // 不设置内容类型
			success : function(data) {
				if(data.code==200){
					getFileList();
				}else {
					alert(data.msg)
				}
			}
		});
}
