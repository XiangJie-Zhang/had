var user1;
$(function () {
    user1 = sessionStorage.getItem('currentUser');
    delExcel();
     uploadExcel();
});


function uploadExcel() {

    var xAxis = new Array();
    var se = new Array();
    var names = new Array();

    $.ajax({
        url : "/had/behavior/echartsNew",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data :{
            user:user1,
            type:'上传'
        },
        async:false,
        success : function(data) {
            if (data.code == 200){
                names = data.content.name;
                xAxis = data.content.date;
                se = data.content.se;
            } else {
                alert(data.msg)
            }
        }
    })
    var mychart = echarts.init(document.getElementById('upload'));
    var option = {
        title: {
            text: '用户删除图'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data:names
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xAxis
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : se
    };
    mychart.setOption(option);
}


function delExcel() {

    var xAxis = new Array();
    var se = new Array();
    var names = new Array();

    $.ajax({
        url : "/had/behavior/echartsNew",// 请求地址
        type : "POST",// 请求类型
        dataType : "json",// 返回类型
        data :{
            user:user1,
            type:'删除'
        },
        async:false,
        success : function(data) {
            if (data.code == 200){
                names = data.content.name;
                xAxis = data.content.date;
                se = data.content.se;
            } else {
                alert(data.msg)
            }
        }
    })
    var mychart = echarts.init(document.getElementById('download'));
    var option = {
        title: {
            text: '用户删除图'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data:names
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xAxis
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : se
    };
    mychart.setOption(option);
}


