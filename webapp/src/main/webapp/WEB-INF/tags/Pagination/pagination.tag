<%@ tag body-content="empty" dynamic-attributes="dynattrs"%>
<%@ attribute type="java.util.List" name="computers" required="true"%>
<%@ attribute name="total" required="true"%>
<%@ attribute name="pageEnCours"%>
<%@ attribute name="filterText"%>
<%@ attribute name="codeTri"%>
<%@ attribute name="interval"%>
<%@ attribute name="nbPage"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Pagination -->
<div class="pagination-centered">


	<!-- Effectue la vraie pagination -->
	<ul class="pagination">

		<li><a class="btn btn-default btn-xs" id="sortDesc"
			href="RedirectIndexServlet?page=${0}&interval=20&filter=${filterText}&codeTri=${codeTri}"><span
				class="glyphicon glyphicon-fast-backward"></span></a></li>
		<c:if test="${ pageEnCours >= 5 }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours-5}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours-5}</a></li>
		</c:if>
		<c:if test="${ pageEnCours >= 4 }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours-4}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours-4}</a></li>
		</c:if>
		<c:if test="${ pageEnCours >= 3 }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours-3}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours-3}</a></li>
		</c:if>
		<c:if test="${ pageEnCours >= 2 }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours-2}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours-2}</a></li>
		</c:if>
		<c:if test="${ pageEnCours >= 1 }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours-1}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours-1}</a></li>
		</c:if>
		<li class="active"><a
			href="RedirectIndexServlet?page=${pageEnCours}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours}</a></li>
		<c:if test="${ pageEnCours + 1 <= nbPage }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours+1}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours+1}</a></li>
		</c:if>
		<c:if test="${ pageEnCours + 2 <= nbPage }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours+2}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours+2}</a></li>
		</c:if>
		<c:if test="${ pageEnCours + 3 <= nbPage }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours+3}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours+3}</a></li>
		</c:if>
		<c:if test="${ pageEnCours + 4 <= nbPage }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours+4}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours+4}</a></li>
		</c:if>
		<c:if test="${ pageEnCours + 5 <= nbPage }">
			<li><a
				href="RedirectIndexServlet?page=${pageEnCours+5}&interval=20&filter=${filterText}&codeTri=${codeTri}">${pageEnCours+5}</a></li>
		</c:if>
		<li><a class="btn btn-default btn-xs" id="sortDesc"
			href="RedirectIndexServlet?page=${nbPage}&interval=20&filter=${filterText}&codeTri=${codeTri}"><span
				class="glyphicon glyphicon-fast-forward"></span></a></li>

	</ul>

</div>
