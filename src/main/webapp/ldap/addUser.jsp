<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>添加用户</title>
</head>
<body>
<div>
    <a href="${pageContext.request.contextPath }/">主页</a>
</div>
<div>${msg}</div>
<div>
    <form action="${pageContext.request.contextPath }/mg/doAddUser?rd=<%=new Random().nextInt(10000)%>" method="post">
        <table>
            <tr>
                <td>登录名</td><td><input type="text" name="uid"/></td>
            </tr>
            <tr>
                <td> 密码</td><td><input type="password" name="pwd"/></td>
            </tr>
            <tr>
                <td>姓</td><td><input type="text" name="sn"/></td>
            </tr>
            <tr>
                <td>名</td><td><input type="text" name="cn"/></td>
            </tr>
            <tr>
                <td>邮箱</td><td><input type="text" name="email"/></td>
            </tr>
        </table>
        <input type="hidden" name="dept" value="${param.dept}"/>
        <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
        <input type="submit" value="Submit"/>
    </form>
</div>
</body>
</html>