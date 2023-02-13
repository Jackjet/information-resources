
$(function () {
    let month = date.getMonth() + 1;
    month = month > 9 ? month : "0" + month;
    axiosAjax('/screen/analysis/getFileAndApiUseCountByMonth').then(res => {
        if (res.code === 1) {
            let datDay = [];//日期
            let datas = [];//数据
            res.data.forEach(item => {
                datDay.push(month + '-' + item.day)
                datas.push(item.useCount)
            })
            echaetsView(datDay, datas);
        }
    })

    function echaetsView(datDay, datas) {
        let myChart = echarts.init(document.getElementById('echarts_1'));
        let option = {
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(1, 71, 143,.3)',
                borderColor: 'rgb(105, 216, 255)',
                textStyle: {
                    color: '#fff'
                }
            },
            grid: {
                top: '10%',
                left: '10%',
                right: '5%',
                bottom: '23%',
            },
            xAxis: [{
                type: 'category',
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false,
                },
                splitArea: {
                    show: false,
                    color: '#f00',
                    lineStyle: {
                        color: '#f00'
                    },
                },
                axisLabel: {
                    color: 'rgb(105, 216, 255)',
                    rotate: '45',//字体倾斜
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: 'rgba(1, 71, 143,.3)',
                        borderWidth: 0.5
                    },
                },
                boundaryGap: false,
                data: datDay,

            }],

            yAxis: [{
                type: 'value',
                minInterval: 1,
                splitLine: {
                    show: false,
                },
                axisLine: {
                    show: false,
                },
                axisLabel: {
                    textStyle: {
                        color: 'rgb(105, 216, 255)',
                    },
                },
                axisTick: {
                    show: false,
                },
            }],
            series: [
                {
                    name: "次数",
                    type: 'line',
                    symbol: 'circle',
                    symbolSize: 6,
                    emphasis: {
                        itemStyle: {
                            borderColor: "rgb(0, 198, 255)",
                            borderWidth: 1
                        }
                    },
                    itemStyle: {
                        color: "rgba(0, 251, 157,.3)"
                    },
                    areaStyle: { //区域填充样式
                        normal: {
                            //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: "rgba(0,129,251,.3)"
                            },
                            {
                                offset: 1,
                                color: "rgba(0,129,251,0)"
                            }
                            ], false),
                            shadowColor: 'rgba(0,129,251,.5)', //阴影颜色
                            shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                        }
                    },
                    data: datas,
                },
            ]
        };

        myChart.setOption(option);
    }

})