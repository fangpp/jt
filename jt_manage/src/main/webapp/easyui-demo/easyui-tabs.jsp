<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/js/jquery-easyui-1.4.1/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="/js/jquery-easyui-1.4.1/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="/css/jt.css" />
<script type="text/javascript" src="/js/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
</head>
<body class="easyui-layout">

  <div data-options="region:'west',title:'菜单',split:true" style="width:10%;">
   		<ul class="easyui-tree">
   			<li>
   			<span>商品管理</span>
   			<ul>
   			<li><a onclick="addTab('商品新增','/item-add')">商品新增</a></li>
   			<li><a onclick="addTab('商品查询','/item-list')">商品查询</a></li>
   			<li><a onclick="addTab('商品更新','/item-update')">商品更新</a></li>
   			</ul>
			</li>
			
			<li>
   			<span>网站内容管理</span>
   			<ul>
   			<li>内容新增</li>
   			<li>内容修改</li>
   			</ul>
			</li>
   		
   		</ul>
   </div>
   <div id="tt" class="easyui-tabs" data-options="region:'center',title:'首页'"></div>


</body>
<script type="text/javascript">
function addTab(title, url){

        var url2="https://map.baidu.com/search/%E5%8C%97%E4%BA%AC%E7%9F%B3%E6%99%AF%E5%B1%B1%E6%B8%B8%E4%B9%90%E5%9B%AD/@12936575.10359031,4826331.968205,16.56z?querytype=s&da_src=shareurl&wd=%E5%8C%97%E4%BA%AC%E7%9F%B3%E6%99%AF%E5%B1%B1%E6%B8%B8%E4%B9%90%E5%9B%AD&c=131&src=0&pn=0&sug=0&l=16&b=(12934842.424322959,4825537.372322239;12938307.78285766,4827126.564087762)&from=webmap&biz_forward=%7B%22scaler%22:1,%22styles%22:%22pl%22%7D&device_ratio=1"
        //画中画效果
        var content = '<iframe scrolling="auto" frameborder="0"  src="'+url2+'" style="width:100%;height:100%;"></iframe>';
        //title: 选项卡标题
        //content : 选项卡中展现的页面信息
        //closable: 选项卡是否可以关闭.
        $('#tt').tabs('add',{
            title:title,
            content:content,  
            closable:true  
        });  
}

</script>

</html>