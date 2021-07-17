<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>写文章</title>
    <link href="${ctx}/css/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"/>

    <link href="${ctx}/css/zui/css/zui.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui-theme.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>
    <style>
        .content {
            position:absolute;
            top:50%;
            left:50%;
            margin:-150px 0 0 -300px;
            width:600px;
            height:300px;

        }
    </style>
</head>
<body>
<div class="content" style="text-align: center">
   <i class="icon-1x icon-check-circle-o" style="color: green"></i> <font size="4">文章发布成功</font>
    <div style="margin-left: 60px">
        <button class="btn btn-block " id="new-dreamland" type="button" style="width: 140px;text-align: center;float: left;margin-top: 40px;background-color: #affbb2">写新文章</button>
        <button class="btn btn-block " id="manage-dreamland" type="button" style="width: 140px;text-align: center;margin-left: 10px;float: left;margin-top: 40px;background-color: #affbb2">管理文章</button>
        <button class="btn btn-block " id="watch-dreamland" type="button" style="width: 140px;text-align: center;margin-left: 10px;float: left;margin-top: 40px;background-color: #affbb2">查看文章</button>
    </div>
</div>
</body>
<script>
    //写新文章
    $("#new-dreamland").click(function () {
        location.href ="${ctx}/writedream"
    });

    //管理文章
    $("#manage-dreamland").click(function () {
        location.href ="${ctx}/list?manage=manage"
    });

    //查看文章
    $("#watch-dreamland").click(function () {
        location.href ="${ctx}/watch?cid=${content.id}"
    });
</script>
</html>