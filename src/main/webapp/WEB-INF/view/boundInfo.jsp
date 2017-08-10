<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>	
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
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
    <form>
        <input id="info_stuNum" class="info_input" type="tel" placeholder="请输入您的学号/工号">
        <input name ="openId" class="info_input" type="hidden" value="${openId}">
        <input id="info_password" class="info_input" type="password" placeholder="请输入密码">
        <div class="verification_code_class">
            <input id="info_verification_code" class="input_verification_code" type="text" placeholder="请输入验证码">
            <div class="verification_code_image">
                <img src="${pageContext.request.contextPath}/static/image/boundInfo/${checkcode}">
            </div>
        </div>
        <input class="info_button" type="button" value="绑定">
        <div class="info_agree">绑定即代表同意本系统相关协议</div>
    </form>
    <div class="bound_info_background"></div>
    <div class="bound_info_class">
        <div class="bound_info_title">
            提示
        </div>
        <div class="bound_info_content">

        </div>
        <input type="button" class="bound_info_btn">
    </div>
</body>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/JS/jQuery/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/JS/jQuery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
    
    jQuery(document).ready(function () {
            var widthOfScreen = jQuery(document).width();
            var heightOfScreen = jQuery(document).height();


            jQuery(".input_verification_code").css("width", 0.78*widthOfScreen - 240 + "px");

            var o_info_stuNum = jQuery("#info_stuNum");
            var o_info_school = jQuery("#info_school");
            var o_info_password = jQuery("#info_password");
            var o_info_verification_code = jQuery("#info_verification_code");

            var o_info_button = jQuery(".info_button");

            var o_bound_info_class = jQuery(".bound_info_class");
            var o_bound_info_content = jQuery(".bound_info_content");
            var o_bound_info_btn = jQuery(".bound_info_btn");

            var o_bound_info_background = jQuery(".bound_info_background");

            o_info_button.click(function () {
                if (o_info_stuNum.val()!="" && o_info_school.val()!="" && o_info_password.val()!="" && o_info_verification_code.val()!="") {
                    o_bound_info_content.empty();
                    
                    jQuery.post("${pageContext.request.contextPath}/grep/login",{password:o_info_password.val(),xh:o_info_stuNum.val(),code:o_info_verification_code.val()},
	                    function(data){
	                    	if(data != ""){
	                            console.log(data);
	                    	 o_bound_info_content.text("恭喜，绑定成功！"+data);
	                         o_bound_info_btn.empty();
	                         o_bound_info_btn.val("OK");
	                         var str = data.substring(1,data.length-1);
	                         jQuery.post("${pageContext.request.contextPath}/common/doBound",{password:o_info_password.val(),xh:o_info_stuNum.val(),openId:"${openId}",name:str})
	                         //可以在里发送ajax去course接口
	                    	}else{
		                    	 o_bound_info_content.text("账号或密码错误");
		                         o_bound_info_btn.empty();
		                         o_bound_info_btn.val("返回");
	                    	}
	                    }	
                    )
                    
                   

                } else {
                    o_bound_info_content.empty();
                    o_bound_info_content.text("不好意思，绑定失败！");

                    o_bound_info_btn.empty();
                    o_bound_info_btn.val("我再去看看");
                }

                o_bound_info_class.animate({top: 350}, 300);
                o_bound_info_background.animate({height: heightOfScreen}, 300);
            });

            o_bound_info_btn.click(function () {
                o_bound_info_class.animate({top: -550}, 300);
                o_bound_info_background.animate({height: 0}, 300);
            });
        });
    </script>
</html>