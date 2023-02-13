
let webUrl_8085 = 'http://8.140.114.226:8085';
let webUrl_8084 = 'http://8.140.114.226:8084';

// let webUrl_8085 = 'http://10.113.0.20:5080';
// let webUrl_8084 = 'http://10.113.0.31:5080';

// ajax函数
function axiosAjax(getUrl) {
    return $.ajax({
        url: webUrl_8085 + getUrl,
        dataType: 'json', //服务器返回json格式数据
        type: 'GET', //HTTP请求类型
        async: false,
        timeout: 10000, //超时时间设置为10秒；
        // success: function (data) {
        //     console.log(data, "success")
        // },
        error: function (err) {
            console.log(err, "error")
        }
    });
}

function axios_Ajax(getUrl) {
    return $.ajax({
        url: webUrl_8084 + getUrl,
        dataType: 'json', //服务器返回json格式数据
        type: 'GET', //HTTP请求类型
        async: false,
        timeout: 10000, //超时时间设置为10秒；
        success: function (data) {
            return data;
        },
        error: function (err) {
            console.log(err, "error")
        }
    });
}

// 获取当前时间
let date = new Date();
let year = date.getFullYear();
let month = date.getMonth() + 1;
let day = date.getDay();
month = month > 9 ? month : "0" + month;
day = day < 10 ? "0" + day : day;
let weekday = new Array(7)
weekday[0] = "星期天"
weekday[1] = "星期一"
weekday[2] = "星期二"
weekday[3] = "星期三"
weekday[4] = "星期四"
weekday[5] = "星期五"
weekday[6] = "星期六"

let time = year + "-" + month + "-" + day + " " + weekday[date.getDay()];
document.getElementById('time_box').innerText = time;