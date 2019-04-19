var kyoPopupMenu = {};
kyoPopupMenu = (function() {
	return {
		sys : function(obj) {
			$('.popup_menu').remove();
			if(sessionStorage.getItem('src')==null){
				popupMenuApp = $('<div class="popup_menu app-menu"><ul>'
						+'<li><a menu="creatFile">新建文件</a></li>'
						+'<li><a menu="creatFloder">新建文件夹</a></li>'
						+'<li><a menu="copy">复制</a></li><li><a menu="cut">剪切</a></li>'
						+'<li><a menu="download">下载</a></li>'
						+'<li><a menu="rename">重命名</a></li>'
						+'<li><a menu="refresh">刷新</a></li>'

						+'</ul></div>')
				.find('a').attr('href', 'javascript:;')
				.end().appendTo('body');
			}else{
				popupMenuApp = $('<div class="popup_menu app-menu">'
									+'<ul>'
									+'<li><a menu="creatFile">新建文件</a></li>'
									+'<li><a menu="creatFloder">新建文件夹</a></li>'
										+'<li><a menu="copy">复制</a></li>'
										+'<li><a menu="cut">剪切</a></li>'
										+'<li><a menu="paste">粘贴</a></li>'
										+'<li><a menu="download">下载</a></li>'
										+'<li><a menu="rename">重命名</a></li>'
										+'<li><a menu="refresh">刷新</a></li>'
									+'</ul>'
								+'</div>')
				.find('a').attr('href', 'javascript:;')
				.end().appendTo('body');
			}

			//绑定事件
			$('.app-menu a[menu="download"]').on('click', function() {
				var src=$(obj).attr("name");
				if (src.lastIndexOf("/")==src.length-1){
					alert("请对准文件下载")
				}
				 var a = document.createElement('a');

				   var url = "downloadFile?path="+src+"&user="+sessionStorage.getItem("currentUser");

				   var filename = src.substring(src.lastIndexOf("/")+1);
				   a.href=url;
				   a.download = filename;
				   a.click()
					updateBehav();
//				 $.ajax({
//					  url: "downloadFile?path="+src,
//					  type: "POST",
//					  data: {},
//						success : function(data) {
//							if(data.code==200){
//
//							}
//						}
//					});
			});
			$('.app-menu a[menu="refresh"]').on('click', function() {
				getFileList();
			});
			$('.app-menu a[menu="rename"]').on('click', function() {
				absolutePath=$(obj).attr("name");
				if(confirm("确认重命名吗？")){
					for (var int = 0; int < vu.list.length; int++) {
						var path = vu.list[int];
						if(absolutePath==path.absolutePath){
							vu.list[int].rename=true;
							id = vu.list[int].name;
						}
					}

				}
			});
			$('.app-menu a[menu="creatFile"]').on('click', function() {
				vu.newFileShow=1;//新建文件
			});
			$('.app-menu a[menu="creatFloder"]').on('click', function() {
				vu.newFileShow=2;//新建文件夹
			});
			$('.app-menu a[menu="copy"]').on('click', function() {
				var src=$(obj).attr("name");
				var bool=false;
				sessionStorage.setItem('src',src);
				sessionStorage.setItem('bool',bool);
			});
			$('.app-menu a[menu="cut"]').on('click', function() {
				var src = $(obj).attr("name");
				var bool = true;
				sessionStorage.setItem('src', src);
				sessionStorage.setItem('bool', bool);
			});
			$('.app-menu a[menu="paste"]').on('click', function() {
				var dest = vu.xpath;
				var src = sessionStorage.getItem('src');
				var bool = sessionStorage.getItem('bool');
				$.ajax({
					url : "/had/innerCopyOrMoveFile",// 请求地址
					type : "POST",// 请求类型
					datatype : "json",// 返回类型
					// contentType :"application/json",//json字符串
					data : {
						bool : bool,
						src : src,
						dest : dest,
						user : sessionStorage.getItem("currentUser")
					},
					success : function(data) {
						if (data.code == 500) {
							 alert(data.msg);
                            updateBehav();
						}
						getFileList();
					}
				})
			});
			return popupMenuApp;
		}
	}
})();
// 取消右键
$('html').on('contextmenu', function() {
	return false;
}).click(function() {
	$('.popup_menu').hide();
});
// 桌面点击右击
$('html')
		.on(
				'contextmenu',
				".table-responsive table tbody tr",
				function(e) {
					var popupmenu = kyoPopupMenu.sys(this);
					l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu
							.width())
							: e.clientX;
					t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu
							.height())
							: e.clientY;
					popupmenu.css({
						left : l,
						top : t
					}).show();
					return false;
				});
$('html')
		.on(
				'contextmenu',
				".table-responsive",
				function(e) {
					var popupmenu = kyoPopupMenu.sys(this);
					l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu
							.width())
							: e.clientX;
					t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu
							.height())
							: e.clientY;
					popupmenu.css({
						left : l,
						top : t
					}).show();
					return false;
				});
