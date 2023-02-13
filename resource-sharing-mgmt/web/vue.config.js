module.exports = {
    //配置css loader
    css: {
        loaderOptions: {
            sass: {
                // sass 版本 9 中使用 additionalData 版本 8 中使用 prependData 旧版本中使用 data
                additionalData: `@import "@/assets/style/main.scss";
                @import "@/assets/style/minxin.scss";`
            }
        }
    },
    // 配置别名
    configureWebpack: {
        resolve: {
            alias: {
                'assets': '@/assets',
                'com': '@/components'
            }
        },
        entry: ['babel-polyfill', './src/main.js']
    },

    // 跨域代理
    devServer: {
        // proxy: {
        //     '/api': {
        //         target: 'http://8.140.114.226:8085/',
        //         changOrigin: true,
        //         pathRewrite: {
        //             '^/api': ''
        //         }
        //     }
        // }
    },
    publicPath: './', // 编译后的地址，可以根据环境进行设置
    // lintOnSave: true, // 是否开启编译时是否不符合eslint提示
}