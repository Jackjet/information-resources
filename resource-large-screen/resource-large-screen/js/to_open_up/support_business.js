$(function () {
    axiosAjax('/screen/analysis/getSupportBusinessTList').then(res => {
        if (res.code === 1) {
            let html = "";
            res.data.forEach(item => {
                html += `<li><span>${item.name}</span><span>${item.num}</span></li>`
            })
            $('.business_list').html(html);
        }
    })
})