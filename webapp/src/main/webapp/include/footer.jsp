	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<c:set var="liste_param">
		<c:forEach items="${ param }" var="p">
			<c:if test="${ p.key != 'language' }">
				&<c:out value="${p.key}" />=<c:out value="${p.value}" />
			</c:if>
		</c:forEach>
	</c:set>

<spring:message code="contents.lang" text="Current language" /> : ${pageContext.response.locale} <br/>
	Language : <a href="?language=en${liste_param}">English</a>|<a href="?language=fr${liste_param}">French</a>
	</body>
</html>