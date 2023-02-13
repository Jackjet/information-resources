// import router from '../router'
import * as d3 from 'd3'

export function isIE() {
  if (!!window.ActiveXObject || 'ActiveXObject' in window) {
    return true
  } else {
    return false
  }
}
function linear(a, d) {
  return function(t) {
    return a + t * d
  }
}
// 全景图
export function createResourceAllView(id, data, scale) {
  var width = $('#' + id).parent().width() * scale
  var height = ($('#' + id).parent().height() - 10) * scale
  var R = height
  var radius = 5
  var i = 0
  var duration = 0
  var lengend = new Map()
  lengend.set('资源', '#2EB7F5')
  lengend.set('无资源', '#A3AEC2')
  lengend.set('API', '#88CA4C')
  lengend.set('SaaS', '#FF7F7F')
  lengend.set('数据文件', '#FFC184')
  lengend.set('目录', '#FFFFFF')
  lengend.set('ODS', '#7EFFFF')
  lengend.set('微应用', '#7FBFFF')
  var strokeLengend = new Map()
  strokeLengend.set('资源', '#289BD0')
  strokeLengend.set('无资源', '#838DA0')
  strokeLengend.set('API', '#588A2A')
  strokeLengend.set('SaaS', '#B93F3F')
  strokeLengend.set('数据文件', '#CC8948')
  strokeLengend.set('目录', '#2EB7F5')
  strokeLengend.set('ODS', '#67D3D3')
  strokeLengend.set('微应用', '#2174C6')
  var tree = d3.layout.tree().size([360, R / 2 - 120])
    .separation(function(a, b) { return (a.parent == b.parent ? 1 : 2) / a.depth })

	   // 定义布局方向
	   var diagonal = d3.svg.diagonal().projection(function(d) {
    var r = d.y; var a = (d.x - 90) / 180 * Math.PI
    return [r * Math.cos(a), r * Math.sin(a)]
									 })

	    var zoom = d3.zoom()
	            .scaleExtent([0.4, 20])
	            .on('zoom', zoomed)

	    var svg = d3.select('#' + id)
	            .attr('width', width)
	            .attr('height', height)
    .append('g')
    .attr('transform', function(d) { return 'translate(' + width / 2 + ',' + height / 2 + ')' })
	            /* .call(zoom)*/
	    var qq = d3.select('g')
	    function zoomed() {
	    	var translate = d3.event.translate
	    	var translatex = translate[0] + width / 2
	    	var translatey = translate[0] + height / 2
	    	// console.log('#####' + translatex)
	    	// console.log('=====' + translatey)
	        qq.attr('transform',
	                'translate(' + translatex + ',' + translatey + ')scale(' + d3.event.scale + ')')
	    }
	    var root = data

  function collapse(d) {
    if (d.children) {
      d._children = d.children
      d._children.forEach(collapse)
      d.children = null
    }
  }

	   // root.children.forEach(collapse)
  update(root)

	    function update(source) {
	        var nodes = tree.nodes(root).reverse()
	                var links = tree.links(nodes)

	        var node = svg.selectAll('g.node')
	                .data(nodes, function(d) { return d.id || (d.id = ++i) })

	        var nodeEnter = node.enter().append('g')
	                .attr('class', 'node')
	                .attr('transform', function(d) {
	                	if (isNaN(d.x)) {
	                		d.x = 0
	                	}
	                	return 'rotate(' + (d.x - 90) + ')translate(' + d.y + ')'
      })
      .on('click', click)

	        nodeEnter.append('circle')
      .attr('style', 'stroke: steelblue stroke-width: 1px cursor:pointer')
      .attr('r', radius)
      .attr('fill', color)

	        nodeEnter.append('text')
      .attr('dy', '.4em')
      .text(
        function(d) {
          var text
          if (d.resourceType.typeName == '目录') {
            if (d.name.length >= 6) {
              text = d.name.substring(0, 6)
              text += '...'
            } else {
              text = d.name
            }
            return text
          } else {
            if (d.name.length >= 12) {
              text = d.name.substring(0, 12)
              text += '...'
            } else {
              text = d.name
            }
            return text
          }
        }
      )
      .attr('text-anchor', function(d) { return d.x < 180 ? 'start' : 'end' })
      .attr('transform', function(d) { return d.x < 180 ? 'translate(8)scale(1.2)' : 'rotate(180)translate(-8)scale(1.2)' })

	        var nodeUpdate = node.transition()
	                .duration(duration)
	                 .attr('transform', function(d) {
	                	 if (isNaN(d.x)) {
		                		d.x = 0
		                	}
	                	 return 'rotate(' + (d.x - 90) + ')translate(' + d.y + ')'
      })
	        nodeUpdate.select('circle')
	                .attr('r', radius)
	                .attr('style', styleFunction)
	                .attr('fill', color)
	                /*	显示折叠
	                 * .attr('fill', function(d, i){
									var png = ''
									if (d.children) {
										png = '/images/common/open.png'
									} else {
										if (d._children) {
											png = '/images/common/close.png'
										} else {
											return '#fff'
										}

									}
									//创建圆形图片
									var defs = svg.append('defs').attr('id', 'imgdefs')

									var catpattern = defs.append('pattern')
															.attr('id', 'catpattern' + i)
															.attr('height', 1)
															.attr('width', 1)

									catpattern.append('image')
											.attr('x', - (img_w / 2 - radius))
											.attr('y', - (img_h / 2 - radius))
											.attr('width', img_w)
											.attr('height', img_h)
											.attr('xlink:href', png)
									return 'url(#catpattern' + i + ')'

								}) */

	        nodeUpdate.select('text')
	                .style('fill-opacity', 1)

	        var nodeExit = node.exit().transition()
	                .duration(duration)
	                .attr('transform', function(d) {
	                	if (isNaN(d.x)) {
	                		d.x = 0
	                	}
	                	return 'rotate(' + (d.x - 90) + ')translate(' + d.y + ')'
      })
	                .remove()

	        nodeExit.select('circle')
	                .attr('r', 10)

	        nodeExit.select('text')
	                .style('fill-opacity', 1e-6)

	        var link = svg.selectAll('path.link')
	                .data(links, function(d) { return d.target.id })

	        link.enter().insert('path', 'g')
	                .attr('class', 'link')

	        link.transition()
	                .duration(duration)
	                .attr('d', diagonal)

	        link.exit().transition()
	                .duration(duration)
	                .attr('d', function(d) {
	                    var o = { x: source.x, y: source.y }
	                    return diagonal({ source: o, target: o })
	                })
	                .remove()

	        nodes.forEach(function(d) {
	            d.x0 = d.x
	            d.y0 = d.y
	        })
	    }
	    function color(d) {
	  	  return lengend.get(d.resourceType.typeName) ? lengend.get(d.resourceType.typeName) : lengend.get('无资源')
	  	}
	    function strokeColor(d) {
		  	  return strokeLengend.get(d.resourceType.typeName) ? strokeLengend.get(d.resourceType.typeName) : strokeLengend.get('无资源')
		 }
	    function styleFunction(d) {
	    	return 'cursor: pointerstroke: ' + strokeColor(d) + 'stroke-width: 1px'
	    }
	    function resAttrClickShow(id, name) {
	    	var content = $('#allview_pop').html()
	    	allview_res_id = id
	    	allview_res_name = name
	    	showTopoDialog('myTopoDialogId', 'myTopoDialogLabel', 'myTopoDialogBodyId', name, content, allviewTableCallback)
	    }
	    function click(d) {
	    	if (d.depth == 3) {
	    	  if (d.resourceType.typeId === '03') {
        router.push({ name: 'serviceDetail', params: { id: d.resId }})
      } else if (d.resourceType.typeId === '02') {
        router.push({ name: 'dataDetail', params: { id: d.resId }})
      } else if (d.resourceType.typeId === '01') {
        router.push({ name: 'apiDetail', params: { id: d.resId }})
      }
	    	} else {
	    		if (d.children) {
		            d._children = d.children
		            d.children = null
		        } else {
		            d.children = d._children
		            d._children = null
		        }
		        update(d)
	    	}
	    }
}

function showLevelTree(nodes, level) {
  nodes.forEach(function(node, d) {
    if (node.depth >= level && undefined != node.children) {
      node._children = node.children
      node.children = null
    }
  })
}
function searchRoot(originalData) {
  var root = {}
  var nodes = originalData.nodes
  nodes.forEach(function(node, j) {
    if (node.id == '-1') {
      root = node
      return
    }
  })
  return root
}
function createTree(root, tree, originalData) {
  tree.name = root.name
  tree.click = root.click
  tree.resourceType = root.resourceType
  originalData.links.forEach(function(link, i) {
    if (root.id == link.resId) {
      originalData.nodes.forEach(function(node, j) {
        if (link.dependencyResId == node.id) {
          createTree(node, node, originalData)
          delete node.id
          if (undefined == tree.children) {
            tree.children = []
          }
          tree.children.push(node)
        }
      })
    }
  })
}

export function createDepartViewTree(list) {
	 var tree = {}
	 tree.name = '资源'
	 tree.resourceType = { 'typeName': '资源' }
	 tree.children = []
	 var departmentMap = new Map()
	 list.forEach(function(node, index) {
		 node.resId = node.id
		 if (!departmentMap.has(node.department.typeName)) {
			 departmentMap.set(node.department.typeName, [node])
		 } else {
      var temp = departmentMap.get(node.department.typeName)
      temp.push(node)
      departmentMap.set(node.department.typeName, temp)
		 }
	 })
	 departmentMap.forEach(function(value, key) {
		  var node0 = {}
		  node0.name = key
		  node0.resourceType = { 'typeName': '目录' }
		  var children0 = []
		  var resourceTypeMap = new Map()
		  value.forEach(function(node, index) {
			  if (!resourceTypeMap.has(node.resourceType.typeName)) {
				 resourceTypeMap.set(node.resourceType.typeName, [node])
			  } else {
				 var temp = resourceTypeMap.get(node.resourceType.typeName)
				 temp.push(node)
				 resourceTypeMap.set(node.resourceType.typeName, temp)
			  }
		  })
		  resourceTypeMap.forEach(function(value1, key1) {
			  var resourceTypeNode = {}
			  resourceTypeNode.name = key1
			  resourceTypeNode.resourceType = { 'typeName': '目录' }
			  var children1 = []
			  value1.forEach(function(node, index) {
				  var node3 = {}
				  node3.name = node.name
				  node3.resId = node.outResId
				  node3.resourceType = node.resourceType
				  //
				  children1.push(node3)
			  })
			  resourceTypeNode.children = children1
			  children0.push(resourceTypeNode)
		  })
		  node0.children = children0
		  tree.children.push(node0)
  })
  return tree
}

export function createCategoryViewTree(list) {
	 var tree = {}
	 tree.name = '资源'
	 tree.resourceType = { 'typeName': '资源' }
	 tree.children = []
	 var categoryMap = new Map()
	 list.forEach(function(node, index) {
		 node.resId = node.id
		 if (!categoryMap.has(node.serviceCategory.typeName)) {
			 categoryMap.set(node.serviceCategory.typeName, [node])
		 } else {
      var temp = categoryMap.get(node.serviceCategory.typeName)
      temp.push(node)
      categoryMap.set(node.serviceCategory.typeName, temp)
		 }
	 })
	 categoryMap.forEach(function(value, key) {
		  var node0 = {}
		  node0.name = key
		  node0.resourceType = { 'typeName': '目录' }
		  var children0 = []
		  var resourceTypeMap = new Map()
		  value.forEach(function(node, index) {
			  if (!resourceTypeMap.has(node.resourceType.typeName)) {
				 resourceTypeMap.set(node.resourceType.typeName, [node])
			  } else {
				 var temp = resourceTypeMap.get(node.resourceType.typeName)
				 temp.push(node)
				 resourceTypeMap.set(node.resourceType.typeName, temp)
			  }
		  })
		  resourceTypeMap.forEach(function(value1, key1) {
			  var resourceTypeNode = {}
			  resourceTypeNode.name = key1
			  resourceTypeNode.resourceType = { 'typeName': '目录' }
			  var children1 = []
			  value1.forEach(function(node, index) {
				  var node3 = {}
				  node3.name = node.name
				  node3.resId = node.resId
				  node3.resourceType = node.resourceType
				  //
				  children1.push(node3)
			  })
			  resourceTypeNode.children = children1
			  children0.push(resourceTypeNode)
		  })
		  node0.children = children0
		  tree.children.push(node0)
  })
  return tree
}

function createAllViewTree(list) {
	 var tree = {}
	 tree.name = '资源'
	 tree.resourceType = { 'typeName': '资源' }
	 tree.children = []
	 var resourceTypeMap = new Map()
	 list.forEach(function(node, index) {
		 node.resId = node.id
		 if (!resourceTypeMap.has(node.resourceType.typeName)) {
			 resourceTypeMap.set(node.resourceType.typeName, [node])
		 } else {
      var temp = resourceTypeMap.get(node.resourceType.typeName)
      temp.push(node)
      resourceTypeMap.set(node.resourceType.typeName, temp)
		 }
	 })

	 resourceTypeMap.forEach(function(value, key) {
		  var node0 = {}
		  node0.name = key
		  node0.resourceType = { 'typeName': key }
		  var children0 = []
		  var serviceCategoryMap = new Map()
		  value.forEach(function(node, index) {
			  if (!serviceCategoryMap.has(node.serviceCategory.typeName)) {
				 serviceCategoryMap.set(node.serviceCategory.typeName, [node])
			  } else {
				 var temp = serviceCategoryMap.get(node.serviceCategory.typeName)
				 temp.push(node)
				 serviceCategoryMap.set(node.serviceCategory.typeName, temp)
			  }
		  })
		  serviceCategoryMap.forEach(function(value1, key1) {
			  var serviceCategoryNode = {}
			  serviceCategoryNode.name = key1
			  serviceCategoryNode.resourceType = { 'typeName': key }
			  var children1 = []
			  value1.forEach(function(node, index) {
				  var node3 = {}
				  node3.name = node.name
				  node3.resId = node.resId
				  node3.resourceType = node.resourceType
				  //
				  children1.push(node3)
			  })
			  serviceCategoryNode.children = children1
			  children0.push(serviceCategoryNode)
		  })
		  node0.children = children0
		  tree.children.push(node0)
  })
  return tree
}

var zoom = d3.zoom().scaleExtent([1, 10])
  .on('zoom', zoomed)

function zoomed() {
  d3.select(this).attr('transform',
    'translate(' + d3.event.translate + ')scale(' + d3.event.scale + ')')
}

function formatterSet2Str(set) {
  var str = ''
  set.forEach(function(node) {
    /* if (node.length >= 5) {
			node = node.substring(5)
			node += '...'
		}*/
    str += node
    str += ' '
  })
  return str
}

// export default{
//   install(Vue){
// Vue.prototype.$createResourceAllView = createResourceAllView
// Vue.prototype.$createDepartViewTree = createDepartViewTree
// Vue.prototype.$createCategoryViewTree = createCategoryViewTree
// Vue.prototype.$isIE = isIE
//   }
// }

