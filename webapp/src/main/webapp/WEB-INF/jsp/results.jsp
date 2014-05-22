<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="../../include/header.jsp" />

<section id="main">
	<h1 id="homeTitle">${ nbComputer } Computers found </h1>
	<div id="actions">
		<form action="SearchComputerServlet" method="GET">
			<input type="search" id="searchbox" name="search" value=""
				placeholder="Search name"> <input type="submit"
				id="searchsubmit" value="Filter by name" class="btn btn-primary">
		</form>
		<a class="btn btn-success" id="add" href="RedirectAddComputerServlet">Add
			Computer</a>
	</div>

	<table class="table table-bordered">
		<thead>
			<tr>
				<!-- Variable declarations for passing labels as parameters -->
				<!-- Table header for Computer Name -->
				<th>Computer Name</th>
				<th>Introduced Date</th>
				<!-- Table header for Discontinued Date -->
				<th>Discontinued Date</th>
				<!-- Table header for Company -->
				<th>Company</th>
				<th></th>
			</tr>
		</thead>
		<tbody>


			<c:forEach items="${ computerList }" var="comp">
				<tr>
					<td><a href="#"><c:out value="${comp.name}" /></td>
					<td><c:out value="${comp.introducedDate}" /></td>
					<td><c:out value="${comp.discontinuedDate}" /></td>
					<td><c:out value="${comp.company.name}" /></td>
					<form action="DeleteComputerServlet" method="post">
						<input type="hidden" name="id" value="${ comp.id }">
						<td><input type="submit" id="deletesubmit" value="Delete"
							class="btn btn-danger"></td>
					</form>

				</tr>
			</c:forEach>

		</tbody>
	</table>

	<center>
		<div id="navPagination">
			<ul class="pagination">
				<li><a href="#">&laquo;</a></li>
				
				
				<c:forEach var="i" begin="1" end="${nbPage}" step="1">
					<li>
						<a href="<c:url value="SearchComputerServlet">
							<c:param name="page" value="${ i }" />
							<c:param name="interval" value="20" />
							<c:param name="search" value="${ search }" />
						</c:url>">${ i }</a>
						
					</li>
				</c:forEach>
				
				<li><a href="#">&raquo;</a></li>
			</ul>
		</div>
	</center>


	<p id="msg_err">${ msg }</p>
</section>

<jsp:include page="../../include/footer.jsp" />
