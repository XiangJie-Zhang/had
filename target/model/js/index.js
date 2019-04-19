

function cambiar_login() {
  document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_login";
document.querySelector('.cont_form_login').style.display = "block";
document.querySelector('.cont_form_sign_up').style.opacity = "0";

setTimeout(function(){  document.querySelector('.cont_form_login').style.opacity = "1"; },400);

setTimeout(function(){
document.querySelector('.cont_form_sign_up').style.display = "none";
},200);
  }

function cambiar_sign_up(at) {
  document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_sign_up";
  document.querySelector('.cont_form_sign_up').style.display = "block";
document.querySelector('.cont_form_login').style.opacity = "0";

setTimeout(function(){  document.querySelector('.cont_form_sign_up').style.opacity = "1";
},100);

setTimeout(function(){   document.querySelector('.cont_form_login').style.display = "none";
},400);


}

$("#last_child").on("click",function(){
  var email = $("#email").val();
    $.ajax({
        url : "/had/user/email",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data : {
            email : email,
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if(data.code == "200"){
                alert("请注意接收")
            }else {
                alert("发送失败")
            }
        }
    })
});
$("#onenext").on("click",function(){
    var is_userName = /^[a-zA-Z]\w{4,9}$/;
    var is_email = /^[1-9a-zA-Z_]\w*@[a-zA-Z0-9]+(\.[a-zA-Z]{2,})+$/;
    var email = $("#email").val();
    var userName = $("#userName").val();
    var realName = $("#realName").val();
    var passwd = $("#passwd").val();
    if (realName==""|| passwd==""){
      alert("不能存在空值！")
    }
    if (!is_userName.test(userName)){
      alert("用户名必须是以字母开头4-9位！")
        return;
    }
    if (!is_email.test(email)) {
      alert("邮箱格式不对!")
        return;
    }

    $.ajax({
        url : "/had/user/registerUser",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data : {
            email : $.trim(email),
            userName : $.trim(userName),
            realName : $.trim(realName),
            passwd : $.trim(passwd)
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if(data.code == "200"){
                alert(data.msg)
                $("#tiao_login").click();
            }else {
              alert(data.msg)
            }
        }
    })
});
$("#login").on("click",function(){
  var userName = $.trim($("#user").val());
  var passwd = $.trim($("#password").val());

    $.ajax({
        url : "/had/user/login",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data : {
            userCode : userName,
            password : passwd
        },
        //async: false,  //是否异步，默认为true
        success : function(data) {
            if(data.code == "200"){
                    sessionStorage.setItem("currentUser", userName);
                    sessionStorage.setItem("currentLocation", "/" + userName + "/");
                window.location.href = "/had/user.html"
            }else {
                alert(data.msg)
            }
        }
    })
});
function ocultar_login_sign_up() {

document.querySelector('.cont_forms').className = "cont_forms";
document.querySelector('.cont_form_sign_up').style.opacity = "0";
document.querySelector('.cont_form_login').style.opacity = "0";

setTimeout(function(){
document.querySelector('.cont_form_sign_up').style.display = "none";
document.querySelector('.cont_form_login').style.display = "none";
},500);

  }
