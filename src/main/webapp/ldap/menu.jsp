<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>menu</title>
  </head>
  <body>
  <a href="${pageContext.request.contextPath }/index.jsp">主页</a>
    <table>
        <thead>
            <tr>
                <th>角色</th><th><a href="${pageContext.request.contextPath }/mg/addRole">添加</a></th><th></th><th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="dn">
            <tr>
                <td></td><td>${dn.cn}</td><td></td><td></td>
            </tr>
                <c:forEach items="${dn.users}" var="u">
                <tr>
                    <td></td><td></td><td>ID:${u.uid};姓名:${u.sn};别名:${u.cn};邮箱:<c:forEach items="${u.emails}" var="mail">${mail}&nbsp;</c:forEach></td>
                    <td><a href="${pageContext.request.contextPath }/mg/fire?dn=${u.dn}&role=${dn.dn}">移除</a></td>
                </tr>
                </c:forEach>
            </c:forEach>
        </tbody>
    </table>
  </body>
</html>