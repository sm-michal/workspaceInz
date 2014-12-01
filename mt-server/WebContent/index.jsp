<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>MOBIL-TAXI</title>
		<link rel="stylesheet" type="text/css" href="css/style.css"/>
	</head>
	<body>
	<div class="container">
		<div class="header">
			<div class="header-left">
				<h1>MOBIL-TAXI</h1>
				<p>Panel administracyjny</p>
			</div>
			<div class="header-right" onclick="document.location='logout';">
			</div>
		</div>
		<div class="content">
			<div class="menuList">
				<ul>
					<c:forEach items="${pageContext.request.userPrincipal.availableMenuItems}" var="menuItem">
						<li><a href="${menuItem.url}"><c:out value="${menuItem.text}"/></a></li>
					</c:forEach>
				</ul>
			</div>
			<div class="innerPage">
				<c:choose>
					<c:when test="${empty includePage}">
						<jsp:include page="jsp/welcomePage.jsp"/>
					</c:when>
					<c:otherwise>
						<jsp:include page="${includePage}"/>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="footer">
			<span>&copy; Michał Smoleń 2014</span>
		</div>
	</div>

	<c:if test="${not empty Exception}">
		<div class="exDiv">
		<c:forEach items="${Exception}" var="ex">
			<div class="singleException">
				<ul><li><c:out value="${ex}"></c:out></li></ul>
			</div>
		</c:forEach>
		
		<c:remove var="Exception"/>
		</div>
	</c:if>
	
	</body>
</html>