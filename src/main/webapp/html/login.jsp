<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
  <head>
    <title>Login</title>
  </head>

  <body onload="document.f.username.focus();">
    <h1>Login</h1>
    <div>
      <a href="${pageContext.request.contextPath }/">主页</a>
    </div>
    <c:if test="${not empty param.login_error}">
      <span style="color: red; ">
        登录失败: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </span>
    </c:if>

    <form name="f" action="${pageContext.request.contextPath }/html/login" method="POST">
      <table>
        <tr><td>User:</td><td><input type='text' name='username' value='<c:if test="${not empty param.login_error}">${param.username}</c:if>'/></td></tr>
        <tr><td>Password:</td><td><input type='password' name='password'></td></tr>
        <tr><td><input type="checkbox" name="remember-me"></td><td>Don't ask for my password for two weeks</td></tr>

        <tr><td colspan='2'><input name="submit" type="submit"></td></tr>
        <tr><td colspan='2'><input name="reset" type="reset"></td></tr>
      </table>
      <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
    </form>

  </body>
</html>