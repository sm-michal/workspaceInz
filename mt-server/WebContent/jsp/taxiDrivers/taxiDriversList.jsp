<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="pl.edu.wszib.msmolen.mt.taxiDrivers.TaxiDriverOperation"%>

<c:set var="EMPLOY" value="<%= TaxiDriverOperation.EMPLOY.getText() %>"/>
<c:set var="REFRESH" value="<%= TaxiDriverOperation.REFRESH.getText() %>"/>
<c:set var="READ" value="<%= TaxiDriverOperation.READ.getText() %>"/>
<c:set var="MODIFY" value="<%= TaxiDriverOperation.MODIFY.getText() %>"/>
<c:set var="FIRE" value="<%= TaxiDriverOperation.FIRE.getText() %>"/>

<fieldset>
<legend>Lista taksówkarzy</legend>

<form action="taxiDrivers" method="POST">
<span class="topButtons">
	<input type="submit" name="action" value="${EMPLOY}" class="textButton"/>
	<input type="submit" name="action" value="${REFRESH}" class="textButton"/>
</span>
</form>
<div class="formTable listTab">
	<div class="formRow listHeader">
		<div class="formCell">&nbsp;</div>
		<c:forEach items="${TaxiDriversBean.columns}" var="column">
			<div class="formCell"><c:out value="${column.displayName}"/></div>
		</c:forEach>
		<div class="formCell">&nbsp;</div>
	</div>
	<c:forEach items="${TaxiDriversBean.data}" var="driver">
	<form action="taxiDrivers" method="POST" class="formRow">
	<input type="hidden" name="driver_id" value="${driver.id}"/>
		<div class="formCell">
			<input type="submit" name="action" class="readButton" value="${READ}" title="Przeglądaj"/>
			<input type="submit" name="action" class="editButton" value="${MODIFY}" title="Modyfikuj"/>
		</div>
		<div class="formCell"><c:out value="${driver.name}"/></div>
		<div class="formCell"><c:out value="${driver.surname}"/></div>
		<div class="formCell"><c:out value="${driver.user.name}"/></div>
		<div class="formCell"><input type="submit" name="action" class="textButton" value="${FIRE}" /></div>
	</form>
	</c:forEach>
</div>
</fieldset>