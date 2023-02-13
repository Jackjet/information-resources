var dcrun_requires = 'require("dc_dcrun");//dc_dcrun基础组件'
var dcrun_classes = 'DCRunService();//dc_dcrun基础组件服务类'
var dcrun_methods = [
  'log(msg);//dc_dcrun基础组件打印日志方法',
  'error(msg);//dc_dcrun基础组件打印错误日志方法',
  'getValueFromConfig(key);//dc_dcrun基础组件从自定义配置里根据key获取值',
  'registerWebApi(type,fun,route);//dc_dcrun基础组件把js函数注册到webapi',
  'getTimestamp();//dc_dcrun基础组件获取时间戳',
  'getAppId();//dc_dcrun基础组件获取AppId',
  'getAppKey();//dc_dcrun基础组件获取AppKey'
]
// 这个js用来从控制中心获取所有组件的属性事件方法得到自动提示的数据集
var initDcRunHint = function (codeMirror, componentsHints) {
  var WORD = /[(\.|\w)$]+/
  codeMirror.registerHelper('hint', 'dcrun', function (editor, options) {
    var word = (options && options.word) || WORD
    var cur = editor.getCursor(),
      curLine = editor.getLine(cur.line)
    var end = cur.ch, start = end
    while (start && word.test(curLine.charAt(start - 1))) --start
    var curWord = start !== end && curLine.slice(start, end)

    var list = (options && options.list) || [], seen = {}
    if (curLine.indexOf('//') >= 0 && curLine.indexOf('//') < end) {
      // 如果前面包含了注释//符号，则没有任何提示
      return {
        list: list,
        from: codeMirror.Pos(cur.line, start),
        to: codeMirror.Pos(cur.line, end)
      }
    }
    var re = new RegExp(word.source, 'g')

    if (componentsHints) {
      var requires = componentsHints.requires === undefined ? [] : componentsHints.requires
      var classes = componentsHints.classes === undefined ? [] : componentsHints.classes
      var methods = componentsHints.methods === undefined ? [] : componentsHints.methods

      // console.log(typeof curWord);
      // console.log("curWord=" + curWord);
      // console.log("curLine=" + curLine + " " + curLine.indexOf(" new "));

      if ((typeof curWord) === 'string') {
        let curWords = curWord.split('\.')
        if (curLine.indexOf('\.') > 0 && curWords && curWords.length > 0) {
          // 如果这一行包含点，则提示的函数必须以点之后输入过的字符串开头
          curWord = curWords[curWords.length - 1]

          // 如果这一行又包含了 new 则可以加上类的构造函数
          if (dcrun_classes.startsWith(curWord) && curLine.indexOf(' new ') >= 0) { // 添加dc_dcrun基础组件
            list.push(getCompletedObject(dcrun_classes,"class"))
          }
          for (var i = 0; i < classes.length; i++) { //
            if (classes[i].startsWith(curWord) && curLine.indexOf(' new ') >= 0) {
              list.push(getCompletedObject(classes[i],"class"))
            }
          }
          
          for (var i = 0; i < methods.length; i++) {
            if (methods[i].startsWith(curWord)) {
              list.push(getCompletedObject(methods[i],"method"))
            }
          }
          for (var i = 0; i < dcrun_methods.length; i++) { // 添加dc_dcrun基础组件
            if (dcrun_methods[i].startsWith(curWord)) {
              list.push(getCompletedObject(dcrun_methods[i],"method"))
            }
          }
        } else { // else 1
          // 如果这一行没有包含点，则提示require组件
          if (dcrun_requires.startsWith(curWord)) {
            list.push(getCompletedObject(dcrun_requires,"require"))
            for (var i = 0; i < requires.length; i++) {
              if (requires[i].startsWith(curWord)) {
                list.push(getCompletedObject(requires[i],"require"))
              }
            }
          }
        }// end else 1
      }
    }
    return {
      list: list,
      from: codeMirror.Pos(cur.line, start),
      to: codeMirror.Pos(cur.line, end)
    }
  })
}
var save = function (code, path) {
  $rxbus.$emit('save', code, path)
}
var combine2List = function (list1, list2) {
  if (list1 && list2) {
    return new Set([...list1.list, ...list2.list])
  } else {
    if (list1) {
      return new Set([...list1.list])
    } else if (list2) {
      return new Set([...list2.list])
    } else {
      return new Set([])
    }
  }
}

var combine3List = function (list1, list2, list3) {
  if (list1 && list2 && list3) {
    return new Set([...list1.list, ...list2.list, ...list3.list])
  } else {
    if (!list1) {
      return this.combine2List(list2, list3)
    } else if (!list2) {
      return this.combine2List(list1, list3)
    } else if (!list3) {
      return this.combine2List(list1, list2)
    }
  }
}

var getCompletedObject = function (text, type) {
  //把text里注释符号//前的作为正文内容，把text作为显示内容，type作为样式标识,method、require、class三种
  if(!text){
    return text
  }
  var texts = text.split("//")
  return {
    text: texts[0],
    displayText: " " + text,
    className: "CodeMirror-dcrun-completion-"+type
  }
}
export default {
  initDcRunHint,
  combine2List,
  combine3List,
  save
}
