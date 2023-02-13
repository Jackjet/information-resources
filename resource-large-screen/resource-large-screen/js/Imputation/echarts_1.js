$(function () {
    // NotionalPoolingData_4
    let myChart = echarts.init(document.getElementById('echarts_1'));
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
        xAxis: {
            type: 'category',
            data: NotionalPoolingData_4.name,
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
        },
        yAxis: {
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
        },
        series: [{
            name: '单位：类',
            type: 'pictorialBar',
            barCategoryGap: '40%',
            symbol: 'triangle',
            barWidth: '40%',
            symbol: 'path://M150 50 L130 130 L170 130 Z',
            itemStyle: {
                normal: {
                    opacity: 0.8,
                    color: 'rgb(0, 251, 157)',
                },
                emphasis: {
                    opacity: 1
                }
            },
            data: NotionalPoolingData_4.total,
            z: 10
        }]
    };
    myChart.setOption(option);
})