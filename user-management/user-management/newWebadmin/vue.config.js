'use strict'
const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
// You can change the port by the following methods:
// port = 9528 npm run dev OR npm run dev --port = 9528
// const port = process.env.port || process.env.npm_config_port || 9528 // dev port
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const IS_PROD = ['production', 'prod'].includes(process.env.NODE_ENV)
const productionGzipExtensions = /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i

// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {

  publicPath: './',
  // filename: '[name].js',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,
  css: {
    loaderOptions: {
      // 给 sass-loader 传递选项
      sass: {
        // 所以这里假设你有 `src/variables.scss` 这个文件
        data: '@import "@/assets/css/variables.scss";'
      }
    }
  },
  configureWebpack: config => {
    const plugins = []
    if (IS_PROD) {
      plugins.push(
        new CompressionWebpackPlugin({
          filename: '[path].gz[query]',
          algorithm: 'gzip',
          test: productionGzipExtensions,
          threshold: 10240,
          minRatio: 0.8
        })
      )
    }
    config.plugins = [...config.plugins, ...plugins];
  },
  chainWebpack(config) {
    config.plugins.delete('preload') // TODO: need test
    config.plugins.delete('prefetch') // TODO: need test
    config.externals({
      'vue': 'Vue',
      'vue-router': 'VueRouter',
      'axios': 'axios',
      'element-ui': 'ELEMENT'
    })
    config.entry.app = ['@babel/polyfill', './src/main.js']
    config.resolve.alias
      .set('@', resolve('src'))
      .end()
    config
    // https://webpack.js.org/configuration/devtool/#development
      .when(process.env.NODE_ENV === 'development',
        config => config.devtool('cheap-source-map')
      )
    config.module
      .rule('images')
      .test(/\.(woff2?|eot|ttf|otf|png|jpe?g|gif|svg)(\?.*)?$/)
      .use('url-loader')
      .loader('url-loader')
      .options({
        limit: 80000,
        esModule: false
      })
    if (IS_PROD) {
      config.module
        .rule('images')
        .test(/\.(woff2?|eot|ttf|otf|png|jpe?g|gif|svg)(\?.*)?$/)
        .use('image-webpack-loader')
        .loader('image-webpack-loader')
        .options({
          mozjpeg: { progressive: true, quality: 65 },
          optipng: { enabled: false },
          pngquant: { quality: [0.65, 0.9], speed: 4 },
          gifsicle: { interlaced: false }
          // webp: { quality: 75 }
        })
    }
  }
}
