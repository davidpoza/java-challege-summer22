<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <form:form action="process-form" modelAttribute="alumno" >
    Name: <form:input path="nombre" />
    <br />
    Apellido: <form:input path="apellido" />
    <button type="submit">Sign up</button>
  </form:form>
</body>
</html>