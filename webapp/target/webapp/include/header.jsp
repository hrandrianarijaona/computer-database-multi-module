<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>EPF Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.0.min.js"> </script>
</head>

<c:set var="liste_param">
	<c:forEach items="${ param }" var="p">
		<c:if test="${ p.key != 'language' }">
			&<c:out value="${p.key}" />=<c:out value="${p.value}" />
		</c:if>
	</c:forEach>
</c:set>

<body>
	<header class="topbar">
	
		<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
			<div class="container">
				<a class="navbar-brand" href="IndexServlet"> <spring:message code="dashboard.header" text="Application - Computer Database" /> </a>
			</div>
			
			<a href="?language=en${liste_param}"><img src="Images/english.gif" /></a>|<a href="?language=fr${liste_param}"><img src="Images/french.jpg" /></a>
			<a class="btn btn-default btn-xs"
					id="sortDesc" href="/logout"><span
						class="glyphicon glyphicon-off"></span></a>
		</nav>
	</header>