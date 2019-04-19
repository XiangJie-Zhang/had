var vue;
var user1;
var type1;
$(function () {
    var param = getRequest();
    user1 = param['user'];
    type1 = param['type'];
    $("#music").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=music")
    $("#office").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=office")
    $("#picture").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=picture")
    $("#mov").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=mov")
    $("#none").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=none")
    $(".nav>li").removeClass("active");
    $("#" + type1).parent('li').addClass("active");
    vue = new Vue({
        el:"#ttt",
        data:{
            list:[]
        }
    })

    getTimeLine();
});
function getTimeLine() {
    $.ajax({
        url : "/had/resources/timeline",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data : {
            user:user1,
            type:type1
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if (data.code == 200){
               vue.list = data.data;
            }else {
                alert(data.msg)
            }
        }
    })
}
// 获取url中参数
function getRequest() {
    var url = location.search;
    var obj = {};
    if (url.indexOf('?') != -1) {
        url = url.substring(url.indexOf('?') + 1).split('&');
        for (var i = 0; i < url.length; i++) {
            var strs = url[i].split('=');
            obj[strs[0]] = unescape(strs[1]);
        }
    }
    return obj;
}

function previewImg1(obj) {
    var data_u = obj.getAttribute("data-url");
    var height = 600; //获取图片高度
    var width = 100; //获取图片宽度
    var imgHtml = "<img src='/had/preview/img?path="+data_u+"' style='height: 600px;width: auto'/>";
    //弹出层
    layer.open({
        type: 1,
        shade: 0.8,
        offset: 'auto',
        area: ['auto',height+'px'],
        shadeClose:true,//点击外围关闭弹窗
        scrollbar: false,//不现实滚动条
        title: "图片预览", //不显示标题
        content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        cancel: function () {
            //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', { time: 5000, icon: 6 });
        }
    });
}


// 弹层视频播放obj
function previewMov(obj) {
    var data_u = obj.getAttribute("data-url");
    var imgHtml = '<video src="/had/preview/mov?path='+data_u+'" controls>\n' +
        '    您的浏览器不支持Video标签。\n' +
        '</video>';
    //弹出层
    layer.open({
        type: 1,
        shade: 0.8,
        offset: 'auto',
        area: ['100%','100%'],
        shadeClose:true,//点击外围关闭弹窗
        scrollbar: false,//不现实滚动条
        title: "视频播放", //不显示标题
        content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        cancel: function () {
            //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', { time: 5000, icon: 6 });
        }
    });
}

/**
 * 预览office文件内容
 * @param data_url
 */
function previewOffice(data_url){
    var data_u = data_url.getAttribute("data-url");
    // encodeURIComponent()
    window.location.href = "/generic/web/viewer.html?file=" + encodeURIComponent("/had/preview/office?path=" + data_u);
}

// 试听音乐
function previewMusic(obj) {
    if ($("#mu").prop("hidden")){
        $("#mu").prop("hidden", false);
    }
    mePlayer.pause();
    var data_u = obj.getAttribute("data-url");
    var name = obj.getAttribute("data-name");
    var url = '/had/preview/music?path=' + data_u;
    $("audio").attr("src", url);
    mePlayer.play();
}

/**
 * 查看文件内容
 * @param data_url
 */
function viewFile(data_url){
    var data_u = data_url.getAttribute("data-url");
    $.ajax({
        url : "/had/readFromHDFSFile",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data :{
            data_u:data_u
        },
        success : function(data) {
            alert(data.content);
        }
    })
}


