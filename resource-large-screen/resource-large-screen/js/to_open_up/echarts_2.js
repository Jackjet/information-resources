$(function () {

    axiosAjax('/screen/analysis/getOrgUseTop').then(res => {
        if (res.code === 1) {
            let names = [];//部门
            let datas = [];//数值
            res.data.forEach(item => {
                names.push(item.orgName)
                datas.push(item.count)
            })
            echaetsView(names, datas);
        }
    })

    function echaetsView(names, datas) {
        let myChart = echarts.init(document.getElementById('echarts_2'));
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
                },
                formatter: (params) => {
                    return `${params[0].name} : ${params[0].value} 条`
                },
            },
            xAxis: {
                type: 'category',
                data: names,
                axisLine: {
                    show: false,
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: 'rgb(105, 216, 255)', //更改坐标轴文字颜色
                    },
                    // rotate: '45',//字体倾斜
                },
            },
            yAxis: {
                minInterval: 1,
                axisLine: {
                    show: false,
                },
                splitLine: {
                    show: false,
                },
                axisLabel: {
                    textStyle: {
                        color: 'rgb(105, 216, 255)', //更改坐标轴文字颜色
                    },
                    formatter: function (value) {
                        return value + "";
                    }
                },
                axisTick: {
                    show: false
                }
            },
            series: [{
                name: '条数',
                type: 'pictorialBar',
                barCategoryGap: '40%',
                symbol: 'triangle',
                barWidth: '30%',
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
                data: datas,
                z: 10
            }]
        };
        myChart.setOption(option);
    }

})