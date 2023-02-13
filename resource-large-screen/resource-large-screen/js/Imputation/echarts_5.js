$(function () {
    axiosAjax('/screen/analysis/getArchUpdateCycCount').then(res => {
        if (res.code === 1) {
            res.data.forEach(item => {
                if (item.num !== '0.00%') {
                    item.value = item.num.slice(0, -1);
                }
            })
            echaetsView(res.data);
        }
    })
    function echaetsView(datas) {
        let myChart = echarts.init(document.getElementById('echarts_5'));
        let option = {
            color: ['rgb(166, 154, 212)', 'rgb(0, 251, 157)', 'rgb(0, 178, 255)', 'rgb(45, 100, 235)', 'rgb(90, 39, 255)', '#00FFFF'],
            series: [
                {
                    name: '数据类型更新周期占比',
                    type: 'pie',
                    radius: ['70%', '90%'],
                    label: {
                        show: true,
                        position: 'left',
                        normal: {
                            show: true,
                            color: '#fff',
                            formatter: (params) => {
                                // return `${params.name} \n ${params.value}%`;
                                return '{a|' + params.name + '}' + '{b|' + params.value + '}%'
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
                    labelLine: {
                        show: false,
                        length: -10
                    },
                    data: datas
                }
            ]
        };
        myChart.setOption(option);
    }

})