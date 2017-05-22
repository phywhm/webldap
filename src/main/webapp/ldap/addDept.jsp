<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>添加部门</title>
</head>
<body>
<div>
    <a href="${pageContext.request.contextPath }/">主页</a>
</div>
<div>${msg}</div>
<div>
    <form action="${pageContext.request.contextPath }/mg/doAddDept?rd=<%=new Random().nextInt(10000)%>" method="post">
        部门<input type="text" name="dept"/>
        <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
        <input type="submit" value="Submit"/>
    </form>
</div>
</body>
</html>