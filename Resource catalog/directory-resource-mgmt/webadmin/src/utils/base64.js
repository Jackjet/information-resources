/**
 * base64二进制数组互转
 */

(function(name, definition) {
  if (typeof define === 'function') {
    // AMD环境或CMD环境
    define(definition)
  } else if (typeof module !== 'undefined' && module.exports) {
    // 定义为普通Node模块
    module.exports = definition()
  } else {
    // 将模块的执行结果挂在window变量中，在浏览器中this指向window对象
    this[name] = definition(axios)
  }
})('Base64', function() {
  var lookup = []
  var revLookup = []
  var Arr = typeof Uint8Array !== 'undefined' ? Uint8Array : Array

  function init() {
    var code = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/'
    for (var i = 0, len = code.length; i < len; ++i) {
      lookup[i] = code[i]
      revLookup[code.charCodeAt(i)] = i
    }

    revLookup['-'.charCodeAt(0)] = 62
    revLookup['_'.charCodeAt(0)] = 63
  }

  init()

  function toByteArray(b64) {
    var i, j, l, tmp, placeHolders, arr
    var len = b64.length

    if (len % 4 > 0) {
      throw new Error('Invalid string. Length must be a multiple of 4')
    }

    // the number of equal signs (place holders)
    // if there are two placeholders, than the two characters before it
    // represent one byte
    // if there is only one, then the three characters before it represent 2 bytes
    // this is just a cheap hack to not do indexOf twice
    placeHolders = b64[len - 2] === '=' ? 2 : b64[len - 1] === '=' ? 1 : 0

    // base64 is 4/3 + up to two characters of the original data
    arr = new Arr(len * 3 / 4 - placeHolders)

    // if there are placeholders, only get up to the last complete 4 chars
    l = placeHolders > 0 ? len - 4 : len

    var L = 0

    for (i = 0, j = 0; i < l; i += 4, j += 3) {
      tmp = (revLookup[b64.charCodeAt(i)] << 18) | (revLookup[b64.charCodeAt(i + 1)] << 12) | (revLookup[b64.charCodeAt(i + 2)] << 6) | revLookup[b64.charCodeAt(i + 3)]
      arr[L++] = (tmp >> 16) & 0xFF
      arr[L++] = (tmp >> 8) & 0xFF
      arr[L++] = tmp & 0xFF
    }

    if (placeHolders === 2) {
      tmp = (revLookup[b64.charCodeAt(i)] << 2) | (revLookup[b64.charCodeAt(i + 1)] >> 4)
      arr[L++] = tmp & 0xFF
    } else if (placeHolders === 1) {
      tmp = (revLookup[b64.charCodeAt(i)] << 10) | (revLookup[b64.charCodeAt(i + 1)] << 4) | (revLookup[b64.charCodeAt(i + 2)] >> 2)
      arr[L++] = (tmp >> 8) & 0xFF
      arr[L++] = tmp & 0xFF
    }

    return arr
  }

  function tripletToBase64(num) {
    return lookup[num >> 18 & 0x3F] + lookup[num >> 12 & 0x3F] + lookup[num >> 6 & 0x3F] + lookup[num & 0x3F]
  }

  function encodeChunk(uint8, start, end) {
    var tmp
    var output = []
    for (var i = start; i < end; i += 3) {
      tmp = (uint8[i] << 16) + (uint8[i + 1] << 8) + (uint8[i + 2])
      output.push(tripletToBase64(tmp))
    }
    return output.join('')
  }

  function fromByteArray(uint8) {
    var tmp
    var len = uint8.length
    var extraBytes = len % 3 // if we have 1 byte left, pad 2 bytes
    var output = ''
    var parts = []
    var maxChunkLength = 16383 // must be multiple of 3

    // go through the array every three bytes, we'll deal with trailing stuff later
    for (var i = 0, len2 = len - extraBytes; i < len2; i += maxChunkLength) {
      parts.push(encodeChunk(uint8, i, (i + maxChunkLength) > len2 ? len2 : (i + maxChunkLength)))
    }

    // pad the end with zeros, but make sure to not forget the extra bytes
    if (extraBytes === 1) {
      tmp = uint8[len - 1]
      output += lookup[tmp >> 2]
      output += lookup[(tmp << 4) & 0x3F]
      output += '=='
    } else if (extraBytes === 2) {
      tmp = (uint8[len - 2] << 8) + (uint8[len - 1])
      output += lookup[tmp >> 10]
      output += lookup[(tmp >> 4) & 0x3F]
      output += lookup[(tmp << 2) & 0x3F]
      output += '='
    }

    parts.push(output)

    return parts.join('')
  }

  function byteBufferToBase64(buffer) {
    // var bytes = new Uint8Array(buffer.toArrayBuffer());
    var bytes = new Uint8Array(buffer)
    return fromByteArray(bytes)
    /*
        var binary = '';
        var len = bytes.byteLength;
        for (var i = 0; i < len; i++) {
            binary += String.fromCharCode( bytes[ i ] );
        }
        return window.btoa( unescape(binary) );
        */
  }
  function base64ToByteBuffer(base64) {
    var bytes = toByteArray(base64)
    /*
    var binary_string = escape(window.atob(base64));
    var len = binary_string.length;
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    */
    return dcodeIO.ProtoBuf.ByteBuffer.wrap(bytes)
  }

  function _Base64() {
    this.byteBufferToBase64 = function(buffer) {
      //   var bString = "";

      //       for(var i = 0, len = buffer.length; i < len; ++i){

      //             bString+= String.fromCharCode(buffer[i]);

      //       }

      //       return btoa(bString);
      return byteBufferToBase64(buffer)
    }
    this.base64ToByteBuffer = function(base64) {
      const map = {
        '0': 52,
        '1': 53,
        '2': 54,
        '3': 55,
        '4': 56,
        '5': 57,
        '6': 58,
        '7': 59,
        '8': 60,
        '9': 61,
        A: 0,
        B: 1,
        C: 2,
        D: 3,
        E: 4,
        F: 5,
        G: 6,
        H: 7,
        I: 8,
        J: 9,
        K: 10,
        L: 11,
        M: 12,
        N: 13,
        O: 14,
        P: 15,
        Q: 16,
        R: 17,
        S: 18,
        T: 19,
        U: 20,
        V: 21,
        W: 22,
        X: 23,
        Y: 24,
        Z: 25,
        a: 26,
        b: 27,
        c: 28,
        d: 29,
        e: 30,
        f: 31,
        g: 32,
        h: 33,
        i: 34,
        j: 35,
        k: 36,
        l: 37,
        m: 38,
        n: 39,
        o: 40,
        p: 41,
        q: 42,
        r: 43,
        s: 44,
        t: 45,
        u: 46,
        v: 47,
        w: 48,
        x: 49,
        y: 50,
        z: 51,
        '+': 62,
        '/': 63
      }

      function base64to2(base64) {
        const len = base64.length * 0.75 // 转换为int8array所需长度
        base64 = base64.replace(/=*$/, '') // 去掉=号（占位的）

        const int8 = new Int8Array(len) // 设置int8array视图
        let arr1
        let arr2
        let arr3
        let arr4
        let p = 0

        for (let i = 0; i < base64.length; i += 4) {
          arr1 = map[base64[i]] // 每次循环 都将base644个字节转换为3个int8array直接
          arr2 = map[base64[i + 1]]
          arr3 = map[base64[i + 2]]
          arr4 = map[base64[i + 3]]
          // 假设数据arr 数据 00101011 00101111 00110011 00110001
          int8[p++] = (arr1 << 2) | (arr2 >> 4)
          // 上面的操作 arr1向左边移动2位 变为10101100
          // arr2 向右移动4位：00000010
          // | 为'与'操作: 10101110
          int8[p++] = (arr2 << 4) | (arr3 >> 2)
          int8[p++] = (arr3 << 6) | arr4
        }
        return int8
      }
      return base64to2(base64)
    }
    // private property
    var _keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/='

    // public method for encoding
    this.encode = function(input) {
      var output = ''
      var chr1, chr2, chr3, enc1, enc2, enc3, enc4
      var i = 0
      input = _utf8_encode(input)
      while (i < input.length) {
        chr1 = input.charCodeAt(i++)
        chr2 = input.charCodeAt(i++)
        chr3 = input.charCodeAt(i++)
        enc1 = chr1 >> 2
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4)
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6)
        enc4 = chr3 & 63
        if (isNaN(chr2)) {
          enc3 = enc4 = 64
        } else if (isNaN(chr3)) {
          enc4 = 64
        }
        output = output +
          _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
          _keyStr.charAt(enc3) + _keyStr.charAt(enc4)
      }
      return output
    }

    // public method for decoding
    this.decode = function(input) {
      var output = ''
      var chr1, chr2, chr3
      var enc1, enc2, enc3, enc4
      var i = 0
      input = input.replace(/[^A-Za-z0-9\+\/\=]/g, '')
      while (i < input.length) {
        enc1 = _keyStr.indexOf(input.charAt(i++))
        enc2 = _keyStr.indexOf(input.charAt(i++))
        enc3 = _keyStr.indexOf(input.charAt(i++))
        enc4 = _keyStr.indexOf(input.charAt(i++))
        chr1 = (enc1 << 2) | (enc2 >> 4)
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2)
        chr3 = ((enc3 & 3) << 6) | enc4
        output = output + String.fromCharCode(chr1)
        if (enc3 != 64) {
          output = output + String.fromCharCode(chr2)
        }
        if (enc4 != 64) {
          output = output + String.fromCharCode(chr3)
        }
      }
      output = _utf8_decode(output)
      return output
    }

    // private method for UTF-8 encoding
    var _utf8_encode = function(string) {
      string = string.replace(/\r\n/g, '\n')
      var utftext = ''
      for (var n = 0; n < string.length; n++) {
        var c = string.charCodeAt(n)
        if (c < 128) {
          utftext += String.fromCharCode(c)
        } else if ((c > 127) && (c < 2048)) {
          utftext += String.fromCharCode((c >> 6) | 192)
          utftext += String.fromCharCode((c & 63) | 128)
        } else {
          utftext += String.fromCharCode((c >> 12) | 224)
          utftext += String.fromCharCode(((c >> 6) & 63) | 128)
          utftext += String.fromCharCode((c & 63) | 128)
        }
      }
      return utftext
    }

    // private method for UTF-8 decoding
    var _utf8_decode = function(utftext) {
      var string = ''
      var i = 0
      var c = c1 = c2 = 0
      while (i < utftext.length) {
        c = utftext.charCodeAt(i)
        if (c < 128) {
          string += String.fromCharCode(c)
          i++
        } else if ((c > 191) && (c < 224)) {
          c2 = utftext.charCodeAt(i + 1)
          string += String.fromCharCode(((c & 31) << 6) | (c2 & 63))
          i += 2
        } else {
          c2 = utftext.charCodeAt(i + 1)
          c3 = utftext.charCodeAt(i + 2)
          string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63))
          i += 3
        }
      }
      return string
    }
  }
  return _Base64
})

