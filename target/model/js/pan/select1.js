/**
 * 查询和新建功能
 */
import ttr from './TodoTrr';
var xvm;
$(function() {
    xvm= new Vue({
        el:"#table1",
        data:{
            list:[],
            xpath:"/",
            newFileShow:0,
            paths:null,
        }
    })
    getFileList();

})

//主页获取目录
function getFileList(){
    $.ajax({
        url : "/had/getFileList",// 请求地址
        type : "POST",// 请求类型
        datatype : "json",// 返回类型
        data : {
            path:xvm.xpath,
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if(data.code==200){
                ttr.list=data.fileList;
                xvm.list=data.fileList;
                var split=xvm.xpath.split("/");
                var p=new Array();
                for (var int = 1; int < split.length-1; int++) {
                    var array = split[int];
                    p[int-1]=array;
                }
                xvm.paths=p;
            }
        }
    })
}

// 获取各种预览(除office)外的二进制流
function getBinary(data_url) {
    var data_u = data_url.getAttribute("data-url");
    var src;
    $.ajax({
        url : "/had/preview/img",// 请求地址
        type : "POST",// 请求类型
        async:false,
        data :{
            path:data_u
        },
        success : function(data) {
            src = data;
        }
    })
    return src;
}


// 弹出图片预览
function previewImg(obj) {
    var data_u = obj.getAttribute("data-url");
    window.open("/had/previewImg.html?path="+data_u);

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

function goTo(obj){
    var path=obj.dataset.path;
    var absolutePath=xvm.xpath.substring(0,xvm.xpath.indexOf(path)+path.length)
    if(!absolutePath.endsWith("/")){
        absolutePath+="/";
    }

    $.ajax({
        url : "/had/getFileList",// 请求地址
        type : "POST",// 请求类型
        datatype : "json",// 返回类型
        data : {
            path:absolutePath,
        },
        success : function(data) {
            if(data.code==200){
                xvm.list=data.fileList;
                xvm.xpath=absolutePath;
                var split=absolutePath.split("/");
                var p=new Array();
                for (var int = 1; int < split.length-1; int++) {
                    var array = split[int];
                    p[int-1]=array;
                }
                xvm.paths=p;
                getFileList();
            }
        }
    })
}
//查看目录
function selectFile(obj){
    var absolutePath = $(obj).attr("data-url");
    var dir = $(obj).attr("data-dir");
    if(dir==""){
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
                    xvm.list=data.fileList;
                    xvm.xpath=absolutePath;

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
                xvm.newFileShow=0;
                xvm.xpath=absolutePath;
                getFileList();
            }
        }
    })
}

//返回上一级
function returnFloder(obj){
    var absolutePath = $(obj).attr("data-xpath");
    if(absolutePath=="/"){
        getFileList();
    }else{
        //absolutePath = absolutePath.substring(0, (absolutePath.length()-1));
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/")+1);
        xvm.xpath = absolutePath;
        getFileList();
    }
}

//刷新
function reflash(obj){
    var absolutePath = $(obj).attr("data-xpath");
    xvm.xpath = absolutePath;
    getFileList();
}

