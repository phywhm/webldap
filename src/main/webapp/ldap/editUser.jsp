<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>设置用户角色</title>
</head>
<body>
<div>
    <a href="${pageContext.request.contextPath }/">主页</a>
</div>
<div>${msg}</div>
<div>
    <form action="${pageContext.request.contextPath }/mg/doModifyUser?rd=<%=new Random().nextInt(10000)%>" method="post">
        登录名<span>${u.uid}</span><br>
        姓<input type="text" name="sn" value="${u.sn}" /><br>
        名<input type="text" name="cn" value="${u.cn}" /><br>
        邮箱
        <c:forEach items="${u.emails}" var="m">
            <input type="text" name="email" value="${m}"/><br>
        </c:forEach>
        <input type="hidden" name="dept" value="${param.dept}"/><br>
        <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
        <input type="submit" value="Submit"/>
    </form>
</div>
</body>
</html>