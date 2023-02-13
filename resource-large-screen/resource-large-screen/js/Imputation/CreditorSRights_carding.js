
let NotionalPoolingData_1 = null;//权责部门总数
let NotionalPoolingData_2 = null;//权责部门统计
let NotionalPoolingData_3 = {};//梳理
let NotionalPoolingData_4 = null;//资源目录梳理提交最多部门TOP10
let NotionalPoolingData_5 = null;
let NotionalPoolingData_6 = null;

let res = axios_Ajax('/api/dashboard?apiKey=7be5499f37034ef5bdc387f5382cd6be').responseJSON
if (res.errno === 0) {
    NotionalPoolingData_1 = res.data.busiCount;
    NotionalPoolingData_2 = res.data.top10BusiType
    NotionalPoolingData_3.infoItemCount = res.data.infoItemCount;//信息项
    NotionalPoolingData_3.deptCount = res.data.deptCount;//部门
    NotionalPoolingData_3.infoCataCount = res.data.infoCataCount;//资源目录

    NotionalPoolingData_4 = res.data.top10SubmitDept[0]
}

let numLen = 6 - NotionalPoolingData_1.toString().length;
let numText = NotionalPoolingData_1.toString();
let liHtml = "";
for (let i = 0; i < 6; i++) {
    if (numLen > i) {
        liHtml += `<li>0</li>`
    }
}
for (let i = 0; i < numText.length; i++) {
    liHtml += `<li>${numText[i]}</li>`
}
$('#CreditorSRights').html(liHtml);


let html_0_5 = '', html_5_10 = '';
NotionalPoolingData_2[0].total.forEach((item, index) => {
    if (index < 5) {
        html_0_5 += `<dl><dt>${item} 项</dt><dd>${NotionalPoolingData_2[0].name[index]}</dd></dl>`;
    } else {
        html_5_10 += `<dl><dt>${item} 项</dt><dd>${NotionalPoolingData_2[0].name[index]}</dd></dl>`;
    }
})
$('#CreditorSRights_type1').html(html_0_5)
$('#CreditorSRights_type2').html(html_5_10)

$('#infoItemCount').text(NotionalPoolingData_3.infoItemCount + "个")
$('#deptCount').text(NotionalPoolingData_3.deptCount + "个部门")
$('#infoCataCount').text(NotionalPoolingData_3.infoCataCount + "类")