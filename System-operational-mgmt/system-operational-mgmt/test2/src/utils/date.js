/*
 * @Author: lyy
 * @Date: 2020-06-09 23:06:26
 * @LastEditTime: 2020-07-01 14:50:47
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 */

/* eslint-disable eol-last */
/* eslint-disable prefer-const */
/**
 * 返回指定时间戳之间的时间间隔
 *  @param {*} startTime 开始时间的时间戳
 *  @param {*} endTime 结束时间的时间戳
 *  @return {string} str 返回时间字符串
 */
export function getTimeInterval(startTime, endTime) {
  let runTime = parseInt((endTime - startTime) / 1000)
  // eslint-disable-next-line prefer-const
  let year = Math.floor(runTime / 86400 / 365)
  runTime = runTime % (86400 * 365)
  let month = Math.floor(runTime / 86400 / 30)
  runTime = runTime % (86400 * 30)
  let day = Math.floor(runTime / 86400)
  runTime = runTime % 86400
  let hour = Math.floor(runTime / 3600)
  runTime = runTime % 3600
  let minute = Math.floor(runTime / 60)
  runTime = runTime % 60
  let second = runTime
  let str = ''
  if (year > 0) {
    str = year + '年'
  }
  if (year <= 0 && month > 0) {
    str = month + '月'
  }
  if (year <= 0 && month <= 0 && day > 0) {
    str = day + '天'
  }
  if (year <= 0 && month <= 0 && day <= 0 && hour > 0) {
    str = hour + '小时'
  }
  if (year <= 0 && month <= 0 && day <= 0 && hour <= 0 && minute > 0) {
    str = minute + '分钟'
  }
  if (year <= 0 && month <= 0 && day <= 0 && hour <= 0 && minute <= 0 && second > 0) {
    str += second + '秒'
  }
  str += '前'
  return str
  // eslint-disable-next-line eol-last
}

/**
 * 判断是否为闰年
 * @param  {number} year 要判断的年份
 * @return {boolean} 返回布尔值
 */
export function leapYear(year) {
  return !(year % (year % 100 ? 4 : 400))
}

/**
 * 返回两个日期之间的差异 (以天为值)。
 * @param  {number} year 要判断的年份
 * @return {boolean} 返回布尔值
 */
export const DiffBetweenDates = (dateInitial, dateFinal) => (dateFinal - dateInitial) / (1000 * 3600 * 24)
// DiffBetweenDates(new Date("2020-12-13"), new Date("2022-12-22")) -> 9

export function getYear() {
  let datetime = new Date();
  let year = datetime.getFullYear();
  let month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1
  let date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate()
  return year + '-' +  month + '-' + date
}

export function getBeforeYear() {
  let dt = new Date()
  dt.setMonth( dt.getMonth()-6 )
  let date= new Date(dt)
  let month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1
  let da = date.getDate() < 10 ? "0" + date.getDate() : date.getDate()
  var dateTime = date.getFullYear() +'-' + month + '-' + da 
  return dateTime
}

// 获取6个月前
export function getweekDay() {
  let dt = new Date()
  dt.setMonth( dt.getMonth()-6 )
  let date= new Date(dt)
  let month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1
  let da = date.getDate() < 10 ? "0" + date.getDate() : date.getDate()
  var dateTime = date.getFullYear() +'-' + month + '-' + da 
  return dateTime
}

// 获取7天前
export function fun_date() { 
  let date1 = new Date();
  let date2 = new Date(date1);
   date2.setDate(date1.getDate() - 7);
   //num是正数表示之后的时间，num负数表示之前的时间，0表示今天
  let month = date2.getMonth() + 1 < 10 ? "0" + (date2.getMonth() + 1) : date2.getMonth() + 1
  let date = date2.getDate() < 10 ? "0" + date2.getDate() : date2.getDate()
  let time2 = date2.getFullYear() + "-" + month + "-" + date
  return time2;
}


export function getWeek(Fn) {
 
  //按周日为一周的最后一天计算
  var date = new Date();

  //今天是这周的第几天
  var today = date.getDay();
  //上周日距离今天的天数（负数表示）
  var stepSunDay = -today + 1;

  // 如果今天是周日
  if (today == 0) {

      stepSunDay = -7;
  }

  // 周一距离今天的天数（负数表示）
  var stepMonday = 7 - today;

  var time = date.getTime();

  var monday = new Date(time + stepSunDay * 24 * 3600 * 1000);
  var sunday = new Date(time + stepMonday * 24 * 3600 * 1000);

  //本周一的日期 （起始日期）
  var startDate = transferDate(monday); // 日期变换
  //本周日的日期 （结束日期）
  var endDate = transferDate(sunday); // 日期变换


  return startDate + ' - ' + endDate;
}


function transferDate(date) {
 
  // 年
  var year = date.getFullYear();
  // 月
  var month = date.getMonth() + 1;
  // 日
  var day = date.getDate();

  if (month >= 1 && month <= 9) {

      month = "0" + month;
  }
  if (day >= 0 && day <= 9) {

      day = "0" + day;
  }

  var dateString = year + '/' + month + '/' + day;

  return dateString;
}
