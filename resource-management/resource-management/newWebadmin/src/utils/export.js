/*
 * @Author: your name
 * @Date: 2020-09-14 15:13:28
 * @LastEditTime: 2020-09-24 11:39:53
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\utils\export.js
 */
import { Loading, MessageBox, Message } from 'element-ui'
import axios from 'axios'
// 导出
export function outExcel(name, url, data, info) {
  const that = this;
  MessageBox.confirm(name, "提示", {
    type: "warning"
  })
    .then(async () => {
      const time = data;
      let formData = new FormData();
      if (time != null) {
        for (var p in time) {
          formData.append(p, time[p]);
        }
      }
      axios({
        method: "post",
        url: process.env.VUE_APP_BASE_API + url,
        data: formData,
        headers: {
          Authorization: "token " + JSON.parse(sessionStorage.getItem("UserInfo")).token,
          "Content-Type": "multipart/form-data"
        },
        responseType: "blob"
      })
        .then(data => {
          if (data.data.type === "application/json") {
            var reader = new FileReader();
            reader.onloadend = function() {
              let res = JSON.parse(reader.result);
              if (res && res.msg) {
                Message.warning(res.msg + "," + res.data);
              }
            };
            reader.readAsText(data.data);
            return;
          }
          let url = window.URL.createObjectURL(new Blob([data.data]));
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", info + ".xlsx");
          document.body.appendChild(link);
          link.click();
          if(info === '用户信息模板'){
            Message.success('模板下载成功')
          }else{
            Message.success('导出成功')
          }
        })
        .catch(data => {
          if(info === '用户信息模板'){
            Message.error('模板下载失败')
          } else {
            Message.error('导出失败')
          }   
        });
    })
    .catch(() => {
      return false;
    });
}