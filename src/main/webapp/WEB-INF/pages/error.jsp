<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="Error">
  <c:if test="empty ${error}">
    <h1> Sorry, an error has occurred </h1>
    </c:if>
  <c:if test="not empty ${error}">
  <h1> An error happened:</h1>
  <h2> ${error} </h2>
  </c:if>
</tags:master>