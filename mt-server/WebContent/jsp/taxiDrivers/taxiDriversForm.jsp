<%@page import="pl.edu.wszib.msmolen.mt.taxiDrivers.TaxiDriverOperation"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="SAVE" value="<%= TaxiDriverOperation.SAVE.getText() %>"/>
<c:set var="CANCEL" value="<%= TaxiDriverOperation.CANCEL.getText() %>"/>

<c:set var="READ" value="<%= TaxiDriverOperation.READ.getText() %>"/>
<c:set var="MODIFY" value="<%= TaxiDriverOperation.MODIFY.getText() %>"/>
<c:set var="NEW" value="<%= TaxiDriverOperation.EMPLOY.getText() %>"/>

<fieldset>
<legend>Dane taksówkarza</legend>
<form action="" method="POST">
<span>
	<c:if test="${operation ne READ}">
	<input type="submit" name="action" value="${SAVE}" class="textButton">
	</c:if>
	<input type="submit" name="action" value="${CANCEL}" class="textButton">
</span>
<input type="hidden" name="driver_id" value="${not empty TaxiDriver ? TaxiDriver.id : '' }" />
<input type="hidden" name="user_id" value="${not empty TaxiDriver ? TaxiDriver.user.id }" />
<input type="hidden" name="operation" value="${operation}"/>
<div class="formTable">
	<div class="formRow">
		<div class="formCell formLabel">Imię</div>
		<div class="formCell">
			<input type="text" name="name" value="${not empty TaxiDriver ? TaxiDriver.name : '' }" ${READ eq operation ? 'disabled' : ''}/>
		</div>
	</div>
	<div class="formRow">
		<div class="formCell formLabel">Nazwisko</div>
		<div class="formCell">
			<input type="text" name="surname" value="${not empty TaxiDriver ? TaxiDriver.surname : '' }" ${READ eq operation ? 'disabled' : ''}/>
		</div>
	</div>
	<div class="formRow">
		<div class="formCell formLabel">Login</div>
		<div class="formCell">
			<input type="text" name="login" value="${not empty TaxiDriver ? TaxiDriver.user.name : '' }" ${READ eq operation ? 'disabled' : ''}/>
		</div>
	</div>
	<div class="formRow">
		<div class="formCell formLabel">Hasło</div>
		<div class="formCell">
			<input type="password" name="password" ${READ eq operation ? 'disabled' : ''}/>
		</div>
	</div>
	<div class="formRow">
		<div class="formCell formLabel">Powtórzenie hasła</div>
		<div class="formCell">
			<input type="password" name="repeatPassword" ${READ eq operation ? 'disabled' : ''}/>
		</div>
	</div>
</div>
</form>
</fieldset>