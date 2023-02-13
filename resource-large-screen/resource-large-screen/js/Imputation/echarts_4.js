$(function () {
    let datas = [];
    axiosAjax('/screen/analysis/getArchMediaFmtCount').then(res => {
        if (res.code === 1) {
            res.data.forEach(item => {
                if (item.num) {
                    datas.push({
                        name: item.name,
                        value: item.num
                    })
                }
            })
            echaetsView(datas);
        }
    })

    function echaetsView(datas) {
        let myChart = echarts.init(document.getElementById('echarts_4'));
        let option = {
            color: ['rgb(166, 154, 212)', 'rgb(0, 251, 157)', 'rgb(0, 178, 255)', 'rgb(45, 100, 235)', 'rgb(90, 39, 255)', '#00FFFF'],
            series: [
                {
                    name: 'Access From',
                    type: 'pie',
                    radius: '75%',
                    label: {
                        show: true,
                        position: 'left',
                        normal: {
                            show: true,
                            color: '#fff',
                            formatter: (params) => {
                                return '{a|' + params.name + '}' + ' {b|' + params.value + '}'
                            },
                            textStyle: {
                                rich: {
                                    a: {
                                        color: 'rgb(104, 182, 255)',
                                    },
                                    b: {
                                        color: '#fff',
                                    },
                                },
                            },
                        },
                    },
                    data: datas,
                }
            ]
        };
        myChart.setOption(option);
    }
})