
var vu;
$(function(){
    $("#music").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=music")
    $("#office").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=office")
    $("#picture").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=picture")
    $("#mov").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=mov")
    $("#none").attr("href", "/had/timeline.html?user="+sessionStorage.getItem("currentUser")+"&type=none")
    getUserMessage()
});

function getUserMessage() {
    let s = sessionStorage.getItem("currentUser");
    if (s != null&& s != ""){
        $.ajax({
            url : "/had/user/getUserMessage",// 请求地址
            type : "POST",// 请求类型
            dataType : "json",// 返回类型
            data : {
                userCode : s
            },
            //async: false,  //是否异步，默认为true
            success : function(data) {
                if(data.code == "200"){
                   $("#initLocation").val(data.content.initLocation);
                   $("#userName").val(data.content.userName);
                   $("#email").val(data.content.email);
                   $("#createTime").val(data.content.createTime);
                   $("#updateTime").val(data.content.updateTime);
                   $("#salt").val(data.content.salt);
                   $("#birthday").val(data.content.birthday);
                   $("#size").val(data.content.roomSize);
                   $("#userId").val(data.content.userId);
                }else {
                    alert(data.msg)
                }
            }
        })
    }
}

$("#updateUser").on("click",function () {

    $.ajax({
        url : "/had/user/updateUser",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data : {
            userId : $("#userId").val(),
            userName : $("#userName").val(),
            birthday : $("#birthday").val()
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if(data.code == "200"){
                sessionStorage.setItem("currentUser", $("#userName").val());
                alert(data.code);
            }else {
                alert(data.msg)
            }
        }
    })
});


