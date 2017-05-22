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
    <form action="${pageContext.request.contextPath }/mg/doSetRole?rd=<%=new Random().nextInt(10000)%>" method="post">
        <c:forEach items="${list}" var="r" varStatus="idx">
            <input type="radio" id="id_${idx.index}" name="role" value="${r.dn}" /><label for="id_${idx.index}">${r.cn}</label><br>
        </c:forEach>
        <input type="hidden" name="dn" value="${param.dn}"/><br>
        <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
        <input type="submit" value="Submit"/>
    </form>
</div>
</body>
</html>