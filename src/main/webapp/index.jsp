<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Your selections</title>
</head>
<body>
<a href="${pageContext.request.contextPath }/html/login.jsp?rd=<%=new Random().nextInt(10000)%>">登录</a>
<a href="javascript:document.forms.lo.submit()">登出</a>
<a href="${pageContext.request.contextPath }/mg/menu?rd=<%=new Random().nextInt(10000)%>">角色</a>
<a href="${pageContext.request.contextPath }/mg/dept?rd=<%=new Random().nextInt(10000)%>">部门</a>
<%
    String selections = (String) session.getAttribute("selections");
    selections = selections == null ? "" : selections;
%>
<div>${msg}</div>
<form name="lo" id="lo" action="${pageContext.request.contextPath }/html/logout?rd=<%=new Random().nextInt(10000)%>"
      method="post"><input type="hidden" name="<c:out value="${_csrf.parameterName}"/>"
                           value="<c:out value="${_csrf.token}"/>"/>
</form>
<form id="sub" name="sub"
      action="${pageContext.request.contextPath }/html/submit.jsp?rd=<%=new Random().nextInt(10000)%>" method="post">
    <p>What do you prefer?</p>
    <p><input type="checkbox" name="car" value="car" <%
        if (selections.indexOf("car") > -1) out.print("checked=\"checked\""); %> />Car</p>
    <p><input type="checkbox" name="bike" value="bike" <%
        if (selections.indexOf("bike") > -1) out.print("checked=\"checked\""); %> />Bike</p>
    <p><input type="checkbox" name="train" value="train" <%
        if (selections.indexOf("train") > -1) out.print("checked=\"checked\""); %> />Train</p>
    <p><input type="checkbox" name="plane" value="plane" <%
        if (selections.indexOf("plane") > -1) out.print("checked=\"checked\""); %> />Plane</p>
    <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>