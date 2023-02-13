$(function () {
    axiosAjax('/screen/analysis/getKeywordSearchTList').then(res => {
        if (res.code === 1) {
            let list = [];
            res.data.forEach(item => {
                list.push({
                    value: item.num,
                    name: item.keyword
                })
            })
            echaetsView(list);
        }
    })

    function echaetsView(list) {
        let myChart = echarts.init(document.getElementById('echarts_3'));
        let option = {
            tooltip: {
                show: true,
                backgroundColor: 'rgba(1, 71, 143,.3)',
                borderColor: 'rgb(105, 216, 255)',
                padding: [10, 15, 10, 15],
                confine: true,
                textStyle: {
                    color: '#fff'
                },
                formatter: (params) => {
                    return `${params.name}：${params.value} 次`
                },
                extraCssText: 'box-shadow: 0 4px 20px -4px rgba(199, 206, 215, .7);border-radius: 4px;'
            },
            series: [
                {
                    type: 'wordCloud',
                    shape: 'pentagon',
                    left: 'center',
                    top: 'center',
                    width: '100%',
                    height: '100%',
                    right: null,
                    bottom: null,
                    sizeRange: [20, 50],
                    rotationRange: [0, 0],
                    rotationStep: 0,
                    gridSize: 25,
                    drawOutOfBound: false,
                    layoutAnimation: true,
                    textStyle: {
                        fontFamily: 'PingFangSC-Semibold',
                        fontWeight: 400,
                        color: 'rgb(1, 215, 253)'
                    },
                    emphasis: {
                        focus: 'none',
                    },
                    data: list,
                },
            ],
        };

        myChart.setOption(option);
    }

})