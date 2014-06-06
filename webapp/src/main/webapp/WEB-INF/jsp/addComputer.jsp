<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../../include/header.jsp" />
<script type="text/javascript" src="js/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>

<script type="text/javascript">
	$(document).ready(
			function() {
				jQuery.validator.addMethod("dateFormat", function(value,
						element) {
					var re = "<spring:message code='validation.pattern' text='/^\d{1,2}-\d{1,2}-\d{4}$/' />";
					return (this.optional(element) && value == "")
							|| re.test(value);
				}, jQuery.validator.format("<spring:message code='validation.format.message' text='Incorrect format' />"));

				jQuery.validator.addMethod("checkDate",
						function(value, element) {
							var elem = value.split('-');
							var day = parseInt(elem["<spring:message code='validation.day.index' text='0' />"]);
							var month = parseInt(elem["<spring:message code='validation.month.index' text='1' />"]);
							var year = parseInt(elem["<spring:message code='validation.year.index' text='2' />"]);
							if (day == 31
									&& (month == 4 || month == 6 || month == 9
											|| month == 11 || month == 04
											|| month == 06 || month == 09)) {
								return false; // only 1,3,5,7,8,10,12 has 31 days
							} else if (month == 2) { // fevrier
								// année bissextile
								if (year % 4 == 0) {
									if (day == 30 || day == 31) {
										return false;
									} else {
										return true;
									}
								} else {
									if (day == 29 || day == 30 || day == 31) {
										return false;
									} else {
										return true;
									}
								}
							} else {
								return true;
							}
						}, "<spring:message code='validation.date.invalid' text='This date is not valid' />");

				jQuery.validator.addMethod("endDate", function(value, element) {
					var startDate = $('#introducedDate').val();
					if ($.trim(value).length > 0)
						return Date.parse(startDate) <= Date.parse(value);
					else
						return true;
				}, "<spring:message code='validation.date.cohesion' text='Discontinued date must be after introduced date' />");

				jQuery.validator.addMethod("requireIntroduced", function(value,
						element) {
					var startDate = $('#introducedDate').val();
					if ($.trim(value).length > 0)
						return $.trim(startDate).length > 0;
					else
						return true;
				}, "<spring:message code='validation.date.intro_req' text='Discontinued date requires an introduced date first' />");
				
				jQuery.validator.addMethod("requireName", function(value,
						element) {
					var theName = $('#name').val();
					if ($.trim(value).length > 0)
						return $.trim(theName).length > 0;
					else
						return true;
				}, "<spring:message code='validation.name.isEmpty' text='The name must not be empty.' />");
				
				jQuery.validator.addMethod("dateComparison", function (value,element) {
					if ( ($.trim(value).length > 0) && ($.trim($('#introducedDate').val()).length > 0) ) { return Date.parse($('#introducedDate').val()) < Date.parse(value); }
					else {return true;}
					},"Dates Impossibles"
					);

				jQuery(document).ready(function() {
					jQuery("#addComputerForm").validate({
						highlight : function(element, errorClass) {
							$(element).fadeOut(function() {
								$(element).fadeIn();
							});
						},
						rules : {
							"name" : {
								"requireName" : true,
								"maxlength" : 255
							},
							"introducedDate" : {
								dateFormat : true,
								checkDate : true
							},
							"discontinuedDate" : {
								dateFormat : true,
								dateComparison: true,
								checkDate : true,
								requireIntroduced : true,
								endDate : true
							}
						}
					});
				});
			});
</script>

<style>
.error {
	color: #ff0000;
}
 
.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>

<c:set var="add">
	<spring:message code="add.button.add" text="Add" />
</c:set>
<c:set var="cancel">
	<spring:message code="add.button.cancel" text="Cancel" />
</c:set>
<c:set var="d_pattern">
	<spring:message code="add.date_pattern" text="\d{1,2}-\d{1,2}-\d{4}" />
</c:set>

<section id="main">

	<h1><spring:message code="add.title" text="Add Computer" /></h1>

	<form:form id="addComputerForm" name="addComputerForm" modelAttribute="computerDTO" method="POST" action="addComputer">
		<form:errors path="*" cssClass="errorblock" element="div" />
		<fieldset>
			<div class="clearfix">
				<label for="name"><spring:message code="add.computer_name" text="Computer name" />:</label>
				<div class="input-group">
					<form:input id="name" type="text" path="name"/>
					<span class="help-inline"><spring:message code="add.required" text="Required" /></span>
					<span><form:errors path="name" cssClass="error" /></span>
				</div>
			</div>

			<div class="clearfix">
				<label for="introduced"><spring:message code="add.introduced" text="Introduced date" />:</label>
				<div class="input-group">
					<form:input path="introducedDate" type="date" id="introducedDate"
						name="introducedDate" pattern="${d_pattern}"/> <span
						class="help-inline"><spring:message code="add.date_format" text="Required" /></span>
						<span><form:errors path="introducedDate" cssClass="error" /></span>
				</div>
			</div>
			<div class="clearfix">
				<label for="discontinued"><spring:message code="add.discontinued" text="Discontinued date" />:</label>
				<div class="input-group">
					<form:input path="discontinuedDate" type="date" id="discontinuedDate"
						name="discontinuedDate" pattern="${d_pattern}"/> <span
						class="help-inline"><spring:message code="add.date_format" text="Required" /></span>
						<span><form:errors path="discontinuedDate" cssClass="error" /></span>
				</div>
			</div>
			<div class="clearfix">
				<label for="company"><spring:message code="add.company" text="Company name" />:</label>
				<div class="input-group">
					
					<form:select path="idCompany" name="idCompany">
						<form:option value="0">--</form:option>
						<c:forEach items="${ companyList }" var="comp">
							<form:option value="${ comp.id }"><c:out
									value="${ comp.name }" /></form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
		</fieldset>
		<div class="actions">
			<input type="submit" value="${ add }" class="btn btn-info"> <spring:message code="add.or" text="or" /> <a
				href="RedirectIndexServlet" class="btn btn-default">${ cancel }</a>
		</div>
	</form:form>

</section>

<jsp:include page="../../include/footer.jsp" />