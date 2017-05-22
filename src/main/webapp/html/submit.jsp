<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Successful</title>
</head>
<body>
<a href="${pageContext.request.contextPath }/index.jsp">Back</a>
<a href="javascript:document.forms.lo.submit()">登出</a>
<form name="lo" id="lo" action="${pageContext.request.contextPath }/html/logout"
      method="post"><input type="hidden" name="<c:out value="${_csrf.parameterName}"/>"
                           value="<c:out value="${_csrf.token}"/>"/>
</form>
<%
  StringBuffer sb = new StringBuffer();
  if (null !=request.getParameter("car"))sb.append("car;");
  if (null !=request.getParameter("bike"))sb.append("bike;");
  if (null !=request.getParameter("train"))sb.append("train;");
  if (null !=request.getParameter("plane"))sb.append("plane;");

  session.setAttribute("selections", sb.toString());
%>
Successful, please go back to check.<br>

</body>
</html>