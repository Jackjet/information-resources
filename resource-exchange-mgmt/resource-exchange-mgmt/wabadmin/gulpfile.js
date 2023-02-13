var path = require('path')
var gulp = require('gulp')
var cleanCSS = require('gulp-clean-css')
var cssWrap = require('gulp-css-wrap')

gulp.task('css-wrap', function () {
  return gulp.src(path.resolve('./theme/index.css'))
    // 找需要添加命名空间的css文件 支持正则表达式
    .pipe(cssWrap({
      // 添加的命名空间
      selector: '.custom-red'
    }))
    .pipe(cleanCSS())
    // 存放的目录
    .pipe(gulp.dest('src/assets/css/theme/red'))
})
