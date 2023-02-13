$(function () {
    axiosAjax('/screen/analysis/getFileAndApiUseCount').then(res => {
        if (res.code === 1) {
            $('#dataPushCount').text(res.data.dataPushCount + "条");
            $('#fileDownCount').text(res.data.fileDownCount + "个");
            $('#apiUseCount').text(res.data.apiUseCount + "个");
        }
    })
})