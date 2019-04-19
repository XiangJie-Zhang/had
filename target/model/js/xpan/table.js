var vu;
var user1;
/**
 * 查询和新建功能
 */
$(function () {
    var param = getRequest();


    vu= new Vue({
        el:"#ttt",
        data:{
            list:[],
            list1:[],
            xpath:"",
            newFileShow:0,
            paths:null,
            vvv:true
        },
        methods : {

        }
    })

    $("#music").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=music")
    $("#office").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=office")
    $("#picture").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=picture")
    $("#mov").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=mov")
    $("#none").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=none")
    getFileList()
    updateBehav()
});

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
//主页获取目录
function getFileList(){
    if (vu.xpath==""){
        if (sessionStorage.getItem("currentUser")=="admin"){
            vu.xpath = "/";

        }else {
            vu.xpath = sessionStorage.getItem("currentLocation");
        }
    }
    // vu.xpath = sessionStorage.getItem("currentLocation");
    $.ajax({
        url : "/had/getFileList",// 请求地址
        type : "POST",// 请求类型
        datatype : "json",// 返回类型
        data : {
            path:vu.xpath,
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if(data.code==200){
                if (data.fileList.length == 0){
                    alert("此文件夹内容为空")
                } else {
                    vu.list=data.fileList;
                    var split=vu.xpath.split("/");
                    var p=new Array();
                    for (var int = 1; int < split.length-1; int++) {
                        var array = split[int];
                        p[int-1]=array;
                    }
                    vu.paths=p;
                }
            }
        }
    })
}

//返回上一级
function returnFloder(obj){
    var absolutePath = $(obj).attr("data-xpath");
    if(sessionStorage.getItem("currentUser")=="admin"&&absolutePath=="/"){
        getFileList();
    }else if (sessionStorage.getItem("currentUser")!="admin"&&absolutePath==sessionStorage.getItem("currentLocation")) {
        getFileList();
    } else {
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/")+1);
        vu.xpath = absolutePath;
        getFileList();
    }
}

/**
 * 取消新建文件或者文件夹
 */
function cancel(){
    vu.newFileShow=0;
}

//查看目录
function selectFile(obj){
    var absolutePath = $(obj).attr("data-url");
    var dir = $(obj).attr("data-dir");
    if(dir){
        $.ajax({
            url : "/had/getFileList",// 请求地址
            type : "POST",// 请求类型
            datatype : "json",// 返回类型
            data : {
                path:absolutePath,
            },
            //async: false,  //是否异步，默认为true
            success : function(data) {
                if(data.code==200){
                    vu.list=data.fileList;
                    vu.xpath=absolutePath;

                    getFileList();
                }
            }
        })
    }else{
        $.ajax({
            url : "/had/readFromHDFSFile",// 请求地址
            type : "POST",// 请求类型
            datatype : "json",// 返回类型
            contentType :"application/json",//json字符串
            data :{
                data_u:absolutePath
            },
            success : function(data) {
                content = data.content;
                alert(data.content);
            }
        })
    }

}

// 弹出图片预览
function previewImg(obj) {
    var data_u = obj.getAttribute("data-url");
    window.open("/had/previewImg.html?path="+data_u);

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

/**
 * 当点击重命名时，使文件名字处出现编辑框
 * @param obj
 */
function rename(obj) {
    absolutePath = $(obj).attr("data-url");
    if(confirm("确认重命名吗？")){
        for (var int = 0; int < vu.list.length; int++) {
            var path = vu.list[int];
            if(absolutePath==path.absolutePath){
                vu.list[int].rename=true;
                id = vu.list[int].name;
            }
        }

    }
}

/**
 * 当编辑框失去焦点时，保存用户输入的新名字
 */
function re(){
    var p = $("input[id='"+id+"']").val();
    var rpath = vu.xpath+p;
    $.ajax({

        url : "/had/renameFile",// 请求地址
        type : "POST",// 请求类型
        datatype : "json",// 返回类型
        data :{
            src:absolutePath,
            dest:rpath,
            user:sessionStorage.getItem('currentUser')
        },
        success : function(data) {
            if (data.code == 200) {
                alert("操作成功")// vm.query()调用其他的方法
                getFileList();
                updateBehav();
            } else {
                alert(data.msg)
            }
        }
    })
}

/**
 * 删除文件功能的js
 */

function del(obj) {
    var absolutePath = $(obj).attr("data-url");
    var index = $(obj).parent().parent('tr').attr('index');

    vu.list.splice(index, 1);
    if(confirm("确认删除吗？")){

        $.ajax({
            url : "/had/delectFromHDFS",// 请求地址
            type : "POST",// 请求类型
            datatype : "json",// 返回类型
            data : {
                path:absolutePath,
                user:sessionStorage.getItem("currentUser")
            },
            success : function(data) {
                if (data.code == 200) {
                    updateBehav();
                } else {
                    alert("操作失败")
                }
            }
        })
    }
}

// 弹出层弹出下载的 二维码
function downloadFromPhone(obj) {
    var data_u = obj.getAttribute("data-url");
    var url = "http://192.168.43.162:8080/had/downloadFile?path="+data_u;
    var divHtml = '<div class="father"><div id="qrcode" class="qrcode son"></div></div>';
    //弹出层
    layer.open({
        type: 1,
        shade: 0.8,
        offset: 'auto',
        area: ['550px','550px'],
        shadeClose:true,//点击外围关闭弹窗
        scrollbar: false,//不现实滚动条
        title: "二维码预览", //不显示标题
        content: divHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        cancel: function () {
            //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', { time: 5000, icon: 6 });
        }
    });
    var qrcode = new QRCode(document.getElementById("qrcode"), {
        text: url,
        width: 256, //生成的二维码的宽度
        height: 256, //生成的二维码的高度
        colorDark : "#000000", // 生成的二维码的深色部分
        colorLight : "#ffffff", //生成二维码的浅色部分
        correctLevel : QRCode.CorrectLevel.H
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
// 弹层图片预览
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

/**
 * 新建文件
 * @param obj
 */
function createFile1(obj){
    var absolutePath = $(obj).attr("data-xpath");
    var fileName = document.getElementById("createFile").value;
    var path= absolutePath +fileName;
    $.ajax({
        url :"/had/createNewFile",//请求地址
        data:{
            path:path,
            user:sessionStorage.getItem("currentUser")
        },
        type: "POST",//请求类型
        dataType: "json", //返回类型
        async: false ,//是否异步，默认为true
        success:function(data){
            if(data.msg == "创建成功!"){
                vu.newFileShow=0;
                alert("创建成功!")
                getFileList();
                updateBehav();
            }else if(data.msg == "创建失败!"){
                alert("创建失败!")
            }else{
                alert("文件已存在!")
            }
        }
    })
}

//创建文件夹
function createDir(obj){
    var absolutePath = $(obj).attr("data-xpath");
    var xfolder = $("#xfloderpath").val();
    var path= absolutePath +xfolder;
    $.ajax({
        url : "/had/createDir",// 请求地址
        type : "POST",// 请求类型
        datatype : "json",// 返回类型
        data : {
            path:path,
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if(data.code==200){
                vu.newFileShow=0;
                vu.xpath=absolutePath;
                getFileList();
                updateBehav();
            }
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
        dataType : "json",// 返回类型
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

function test(){
    var path=vu.xpath;
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
                updateBehav();
            }else {
                alert(data.msg)
            }
        }
    });
}


function updateBehav() {
    var user = sessionStorage.getItem('currentUser');
    $.ajax({
        url: "/had/updateBehavior",
        type: "POST",
        data: {
          user: user
        },
        dataType:"json",
        success : function(data) {
            if(data.code==200){
                vu.list1 = data.content;
            }else {
                alert(data.msg)
            }
        }
    });
}
