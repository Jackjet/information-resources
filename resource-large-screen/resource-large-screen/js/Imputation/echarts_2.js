$(function () {

    axiosAjax('/screen/analysis/getDataCountTList').then(res => {
        if (res.code === 1) {
            let name = [[], []], datas = [[], []], datas1 = [[], []], datas2 = [[], []];
            res.data.forEach((item, index) => {
                if (index < 5) {
                    name[0].push(item.orgName)
                    datas[0].push(item.api)
                    datas1[0].push(item.file)
                    datas2[0].push(item.data)
                } else {
                    name[1].push(item.orgName)
                    datas[1].push(item.api)
                    datas1[1].push(item.file)
                    datas2[1].push(item.data)
                }
            })
            echaetsView(name, datas, datas1, datas2);
        }
    })

    function echaetsView(name, datas, datas1, datas2) {
        let myChart = echarts.init(document.getElementById('echarts_2'));

        var xAxis = [],
            yAxis = [],
            series = [];
        for (var i = 0; i < 2; i++) {
            xAxis.push({
                gridIndex: i,
                show: false,
                type: 'value'
            })
            yAxis.push({
                gridIndex: i,
                type: 'category',
                inverse: true,
                axisLabel: {
                    show: true,
                    align: 'right',
                    textStyle: {
                        fontSize: 14,
                        color: 'rgb(105, 216, 255)'
                    }
                },
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                data: name[i]
            })
            yAxis.push({
                gridIndex: i,
                type: 'category',
                inverse: true,
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                axisLabel: {
                    show: false
                },
                data: datas[i].map(item => item.value)
            })
            series.push({
                name: "接口（个）",
                type: 'bar',
                barWidth: 10,
                stack: [2 * i, 2 * (i + 1) - 1],
                xAxisIndex: i,
                yAxisIndex: [2 * i, 2 * (i + 1) - 1],
                itemStyle: {
                    color: 'rgb(166, 154, 212)',
                    // barBorderRadius: 5
                },
                zlevel: 1,
                data: datas[i]
            })
            series.push({
                name: "文件（个）",
                type: 'bar',
                barWidth: 10,
                stack: [2 * i, 2 * (i + 1) - 1],
                xAxisIndex: i,
                yAxisIndex: [2 * i, 2 * (i + 1) - 1],
                itemStyle: {
                    color: 'rgb(0, 112, 255)',
                    // barBorderRadius: 5
                },
                zlevel: 1,
                data: datas1[i]
            })
            series.push({
                name: "数据（条）",
                type: 'bar',
                barWidth: 10,
                stack: [2 * i, 2 * (i + 1) - 1],
                xAxisIndex: i,
                yAxisIndex: [2 * i, 2 * (i + 1) - 1],
                itemStyle: {
                    color: 'rgb(0, 251, 157)',
                    // barBorderRadius: 5
                },
                zlevel: 1,
                data: datas2[i]
            })
        }

        let option = {
            legend: {
                show: false
            },
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(1, 71, 143,.3)',
                borderColor: 'rgb(105, 216, 255)',
                textStyle: {
                    color: '#fff'
                },
            },
            grid: [{
                x: 0,
                y: 0,
                top: '10%',
                width: '45%',
                height: '100%',
                containLabel: true
            }, {
                x2: 0,
                y: 0,
                top: '10%',
                left: '50%',
                width: '45%',
                height: '100%',
                containLabel: true
            }],
            yAxis: yAxis,
            xAxis: xAxis,
            series: series
        }

        myChart.setOption(option);
    }

})