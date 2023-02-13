$(function () {
    let res = axios_Ajax('/api/dashboard?apiKey=7be5499f37034ef5bdc387f5382cd6be').responseJSON
    let openRate = '';//开放
    let shareRate = '';//共享
    if (res.errno === 0) {
        openRate = res.data.openRate * 100;
        shareRate = res.data.shareRate * 100;
    }
    let myChart = echarts.init(document.getElementById('echarts_6'));
    let option = {
        series: [
            {
                name: "共享度外围刻度", //系列名称,用于tooltip的显示
                type: "gauge", //仪表盘
                center: ["25%", "55%"], // 默认全局居中
                radius: "80%",
                axisLine: {
                    show: false,
                    lineStyle: {
                        width: 5,
                        opacity: 0, //盘的颜色变成透明
                    }
                },
                splitLine: {
                    show: false,
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        width: 1,
                        color: 'rgb(0, 112, 255)',
                    },
                    length: -5,
                    distance: -20,
                },
                pointer: {
                    show: false
                },
                title: {
                    show: false
                },
                axisLabel: {
                    show: false
                },
                detail: {
                    show: false
                },
            },
            {
                name: "共享度", //系列名称,用于tooltip的显示
                type: "gauge", //仪表盘
                center: ["25%", "55%"], // 默认全局居中
                radius: "100%",
                axisLine: {
                    //仪表盘轴线
                    lineStyle: {
                        //轴线不同的颜色段
                        color: [
                            [0, "rgb(166, 154, 212)"],
                            [0.33, "rgb(166, 154, 212)"],
                            [0.66, "rgb(0, 112, 255)"],
                            [1, "rgb(0, 251, 157)"]
                        ],
                        width: 5 //轴线宽度
                    }
                },
                splitLine: {
                    show: false,
                },
                axisTick: {
                    show: false,
                },
                pointer: {
                    itemStyle: {
                        color: "auto"
                    },
                    width: 2
                },
                title: {
                    textStyle: {
                        fontSize: 14,
                        color: "rgb(105, 216, 255)"
                    },
                    offsetCenter: [0, "80%"]
                },
                axisLabel: {
                    show: false
                },
                detail: {
                    color: "rgb(105, 216, 255)",
                    fontSize: 16,
                    formatter: '{value} %',
                    offsetCenter: [0, "-50%"]
                },
                data: [{ value: shareRate, name: "共享度" }]
            },
            {
                name: '共享度内环刻度',
                type: 'gauge',
                startAngle: 225, //起始角度，同极坐标
                endAngle: -45,
                center: ["25%", "55%"], // 默认全局居中
                radius: "110%",
                axisLine: {
                    show: false,
                    lineStyle: {
                        width: 5,
                        opacity: 0, //盘的颜色变成透明
                    }
                },
                axisLabel: {
                    show: false,
                },
                axisTick: {
                    show: true,
                    splitNumber: 5,
                    length: 3,
                    lineStyle: {
                        color: 'rgb(0, 112, 255)', //用颜色渐变函数不起作用
                        width: 1,
                    },
                },
                splitLine: {
                    show: false,
                },
                detail: {
                    show: false,
                },
                pointer: {
                    show: false,
                },
            },
            {
                name: "共享度外围刻度", //系列名称,用于tooltip的显示
                type: "gauge", //仪表盘
                center: ["70%", "55%"], // 默认全局居中
                radius: "80%",
                axisLine: {
                    show: false,
                    lineStyle: {
                        width: 5,
                        opacity: 0, //盘的颜色变成透明
                    }
                },
                splitLine: {
                    show: false,
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        width: 1,
                        color: 'rgb(0, 112, 255)',
                    },
                    length: -5,
                    distance: -20,
                },
                pointer: {
                    show: false
                },
                title: {
                    show: false
                },
                axisLabel: {
                    show: false
                },
                detail: {
                    show: false
                },
            },
            {
                name: "共享度", //系列名称,用于tooltip的显示
                type: "gauge", //仪表盘
                center: ["70%", "55%"], // 默认全局居中
                radius: "100%",
                axisLine: {
                    //仪表盘轴线
                    lineStyle: {
                        //轴线不同的颜色段
                        color: [
                            [0, "rgb(166, 154, 212)"],
                            [0.33, "rgb(166, 154, 212)"],
                            [0.66, "rgb(0, 112, 255)"],
                            [1, "rgb(0, 251, 157)"]
                        ],
                        width: 5 //轴线宽度
                    }
                },
                splitLine: {
                    show: false,
                },
                axisTick: {
                    show: false,
                },
                pointer: {
                    itemStyle: {
                        color: "auto"
                    },
                    width: 2
                },
                title: {
                    textStyle: {
                        fontSize: 14,
                        color: "rgb(105, 216, 255)"
                    },
                    offsetCenter: [0, "80%"]
                },
                axisLabel: {
                    show: false
                },
                detail: {
                    color: "rgb(105, 216, 255)",
                    fontSize: 18,
                    formatter: '{value} %',
                    offsetCenter: [0, "-50%"]
                },
                data: [{ value: openRate, name: "开放度" }]
            },
            {
                name: '共享度内环刻度',
                type: 'gauge',
                startAngle: 225, //起始角度，同极坐标
                endAngle: -45,
                center: ["70%", "55%"], // 默认全局居中
                radius: "110%",
                axisLine: {
                    show: false,
                    lineStyle: {
                        width: 5,
                        opacity: 0, //盘的颜色变成透明
                    }
                },
                axisLabel: {
                    show: false,
                },
                axisTick: {
                    show: true,
                    splitNumber: 5,
                    length: 3,
                    lineStyle: {
                        color: 'rgb(0, 112, 255)', //用颜色渐变函数不起作用
                        width: 1,
                    },
                },
                splitLine: {
                    show: false,
                },
                detail: {
                    show: false,
                },
                pointer: {
                    show: false,
                },
            },
        ]
    };

    myChart.setOption(option);
})