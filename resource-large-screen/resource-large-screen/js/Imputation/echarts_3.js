
$(function () {

    axiosAjax('/screen/analysis/getArchAndDataCountTList').then(res => {
        if (res.code === 1) {
            let names = [], data = [], datas = [];
            res.data.forEach(item => {
                names.push(item.orgName)
                data.push(item.count)
                datas.push(item.data)
            })
            echaetsView(names, data, datas);
        }
    })
    function echaetsView(names, data, datas) {
        let myChart = echarts.init(document.getElementById('echarts_3'));

        let option = {
            grid: {
                top: '10%',
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(1, 71, 143,.3)',
                borderColor: 'rgb(105, 216, 255)',
                textStyle: {
                    color: '#fff'
                }
            },
            xAxis: [
                {
                    type: 'category',
                    axisLine: {
                        show: false,
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        textStyle: {
                            color: 'rgb(105, 216, 255)', //更改坐标轴文字颜色
                        }
                    },
                    data: names
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLine: {
                        show: false,
                    },
                    splitLine: {
                        show: false,
                    },
                    axisLabel: {
                        textStyle: {
                            color: 'rgb(105, 216, 255)', //更改坐标轴文字颜色
                        }
                    },
                    axisTick: {
                        show: false
                    }
                }
            ],
            series: [
                {
                    name: '目录数（条）',
                    type: 'bar',
                    barGap: 0,
                    barWidth: '15%',
                    emphasis: {
                        focus: 'series',
                    },
                    itemStyle: {
                        normal: {
                            color: 'rgb(0, 178, 255)',
                        },
                    },
                    data: data
                },
                {
                    name: '提供量（条）',
                    type: 'bar',
                    barWidth: '15%',
                    emphasis: {
                        focus: 'series'
                    },
                    itemStyle: {
                        normal: {
                            color: 'rgb(0, 251, 157)',
                        },
                    },
                    data: datas
                },
            ]
        };

        myChart.setOption(option);
    }

})