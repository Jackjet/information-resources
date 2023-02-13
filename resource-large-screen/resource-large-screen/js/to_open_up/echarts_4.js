$(function () {

    axiosAjax('/screen/analysis/getOrgApplyTop').then(res => {
        // console.log(res, "部门积极度排名")
        if (res.code === 1) {
            let success = [];//成功
            let haveInHand = [];//进行中
            let error = [];//失败
            let names = [];//部门
            res.data.forEach(item => {
                success.push(item.approved)
                haveInHand.push(item.init)
                error.push(item.fail)
                names.push(item.orgName)
            })
            echaetsView(success, haveInHand, error, names);
        }
    })

    function echaetsView(success, haveInHand, error, names) {
        let myChart = echarts.init(document.getElementById('echarts_4'));
        let option = {
            color: ['rgb(0, 251, 157)', 'rgb(0, 112, 255)', 'rgb(166, 154, 212)'],// 手动指定柱状图颜色
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(1, 71, 143,.3)',
                borderColor: 'rgb(105, 216, 255)',
                textStyle: {
                    color: '#fff'
                }
            },
            legend: {
                selectedMode: false,//取消图例上的点击事件
                textStyle: {
                    color: 'rgb(105, 216, 255)',
                },
                data: ['未审核', '已审核', '审核失败'],
            },
            grid: {
                top: '20%',
                left: '0%',
                right: '0%',
                bottom: '5%',
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                axisTick: false,// 不显示x轴的刻度
                axisLabel: {
                    color: 'rgb(105, 216, 255)'
                },
                axisLine: {
                    show: false,
                },
                data: names
            }],
            yAxis: [{
                type: 'value',
                minInterval: 1,
                axisTick: false,// 不显示y轴的刻度
                axisLabel: {
                    color: 'rgb(105, 216, 255)'
                },
                axisLine: {
                    show: false,
                },
                splitLine: {
                    show: false
                },
            }],
            series: [
                {
                    name: '未审核（项）',
                    type: 'bar',
                    stack: '饱和度',
                    barWidth: 10,
                    data: haveInHand
                },
                {
                    name: '已审核（项）',
                    type: 'bar',
                    stack: '饱和度',
                    barWidth: 10,
                    data: success
                },
                {
                    name: '审核失败（项）',
                    type: 'bar',
                    stack: '饱和度',
                    barWidth: 10,
                    data: error
                }

            ]
        };
        myChart.setOption(option);
    }

})