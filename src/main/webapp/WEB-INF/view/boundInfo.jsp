<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>	
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>绑定信息</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/CSS/boundInfo/boundInfo.css">
</head>
<body>
    <div class="top_view">
        <img src="${pageContext.request.contextPath}/static/image/boundInfo/pigeons.png">
    </div>
    <div class="top_info">
        欢迎使用云鸽子系统，请您绑定！
    </div>
    <form action="${pageContext.request.contextPath}/common/doBound" method="post">
        <input name ="xh" class="info_input" type="tel" placeholder="请输入您的学号/工号">
        <input name ="openId" class="info_input" type="hidden" value="${openId}">
        <input name="password" class="info_input" type="password" placeholder="请输入密码">
        <input class="info_button" type="submit" value="绑定">
        <div class="info_agree">绑定即代表同意本系统相关协议</div>
    </form>
</body>
</html>