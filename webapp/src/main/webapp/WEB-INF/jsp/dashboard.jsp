<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="pg" tagdir="/WEB-INF/tags/Pagination"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<jsp:include page="../../include/header.jsp" />

<c:set var="edit">
	<spring:message code="dashboard.button.edit" text="Edit" />
</c:set>
<c:set var="delete">
	<spring:message code="dashboard.button.delete" text="Delete" />
</c:set>
<c:set var="search">
	<spring:message code="dashboard.button.search" text="Search" />
</c:set>
<c:set var="format_date">
	<spring:message code="dashboard.format_date" text="yyyy/MM/dd" />
</c:set>

<section id="main">
	<h1 id="homeTitle">${ pageComputer.totalCount } <spring:message code="dashboard.computers_found" text="computers found" /></h1>

	<div id="actions">
		<form action="IndexServlet" method="GET">
			<input type="search" id="searchbox" name="filter" value=""
				placeholder="Search name"> <input type="submit"
				id="searchsubmit" value="${ search }" class="btn btn-primary">
		</form>
		<a class="btn btn-success" id="add" href="addComputer"><spring:message code="dashboard.button.add" text="Add Computer" /></a>
	</div>

	<table class="table table-bordered">
		<thead>
			<tr>
				<!-- Variable declarations for passing labels as parameters -->
				<!-- Table header for Computer Name -->
				<th><spring:message code="dashboard.computer_name" text="Computers name" /> <a class="btn btn-default btn-xs"
					id="sortDesc" href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=0"><span
						class="glyphicon glyphicon-sort-by-alphabet"></span></a> <a
					class="btn btn-default btn-xs" id="sortDesc"
					href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=1"><span
						class="glyphicon glyphicon-sort-by-alphabet-alt"></span></a>

				</th>
				<th><spring:message code="dashboard.introduced" text="Introduced date" /> <a class="btn btn-default btn-xs"
					id="sortDesc" href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=2"><span
						class="glyphicon glyphicon-arrow-down"></span></a> <a
					class="btn btn-default btn-xs" id="sortDesc"
					href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=3"><span
						class="glyphicon glyphicon-arrow-up"></span></a></th>
				<!-- Table header for Discontinued Date -->
				<th><spring:message code="dashboard.discontinued" text="Discontinued date" /> <a class="btn btn-default btn-xs"
					id="sortDesc" href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=4"><span
						class="glyphicon glyphicon-arrow-down"></span></a> <a
					class="btn btn-default btn-xs" id="sortDesc"
					href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=5"><span
						class="glyphicon glyphicon-arrow-up"></span></a></th>
				<!-- Table header for Company -->
				<th><spring:message code="dashboard.company" text="Company" /> <a class="btn btn-default btn-xs" id="sortDesc"
					href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=6"><span
						class="glyphicon glyphicon-sort-by-alphabet"></span></a> <a
					class="btn btn-default btn-xs" id="sortDesc"
					href="RedirectIndexServlet?page=0&interval=${pageComputer.pageLimit}&filter=${pageComputer.filter}&codeTri=7"><span
						class="glyphicon glyphicon-sort-by-alphabet-alt"></span></a></th>
				<th></th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${ pageComputer.liste }" var="comp">
				<tr>
					<td><a href="#"><c:out value="${comp.name}" /></td>
					<td><joda:format value="${comp.introducedDate}" pattern="${ format_date }" /></td>
					<td><joda:format value="${comp.discontinuedDate}" pattern="${ format_date }" /></td>
					<td><c:out value="${comp.company.name}" /></td>
					<form action="editComputer" method="get">
						<input type="hidden" name="id" value="${ comp.id }">
						<td><input type="submit" id="editsubmit" value=" ${ edit } "
							width="60" class="btn btn-info"></td>
					</form>
					<form action="deleteComputer" method="post">
						<input type="hidden" name="id" value="${ comp.id }">
						<td><input type="submit" id="deletesubmit" value=" ${ delete } "
							class="btn btn-danger"></td>
					</form>

				</tr>
			</c:forEach>

		</tbody>
	</table>

	<pg:pagination computers="${ pageComputer.liste }" total="${ pageComputer.totalCount }"
		pageEnCours="${ pageComputer.currentPage }"	filterText="${ pageComputer.filter }" codeTri="${ pageComputer.codeTri }" interval="${ pageComputer.pageLimit }" nbPage="${ pageComputer.nbPage }" />
 

	<p id="msg_err">${ msg }</p>
</section>

<jsp:include page="../../include/footer.jsp" />
