<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>dept</title>
  </head>
  <body>
  <div>
      <a href="${pageContext.request.contextPath }/index.jsp">主页</a>
  </div>
    <table>
        <thead>
            <tr>
                <th>部门</th><th><a href="${pageContext.request.contextPath }/mg/addDept">添加</a></th><th></th><th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="dn">
            <tr>
                <td></td><td>${dn.ou}</td><td><span><a href="${pageContext.request.contextPath}/mg/addUser?dept=${dn.ou}">添加用户</a></span></td><td></td>
            </tr>
                <c:forEach items="${dn.users}" var="u">
                <tr>
                    <td></td><td></td><td dn="${u.dn}">ID:${u.uid};姓:${u.sn};名:${u.cn};邮箱:<c:forEach items="${u.emails}" var="mail">${mail}&nbsp;</c:forEach></td>
                    <td><c:choose><c:when test="${not empty u.roleName}">${u.roleName}</c:when><c:otherwise><a href="${pageContext.request.contextPath}/mg/setRole?dn=${u.dn}">设置角色</a></c:otherwise></c:choose> </td>
                </tr>
                </c:forEach>
            </c:forEach>
        </tbody>
    </table>
  </body>
</html>