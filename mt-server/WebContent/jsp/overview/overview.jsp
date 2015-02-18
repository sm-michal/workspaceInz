<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<p>Tutaj będzie applet</p>

<!-- 
<applet code="pl.edu.wszib.msmolen.mt.applet.OverviewApplet"
	archive="https://localhost:8443/mt-server/applet/mt-applet.jar"
	width="300"
	height="300">
	<param name="permissions" value="sandbox"/>
</applet>
 -->
 
<p>... albo google map :)</p>

<div id="googleMapDiv">
</div>

<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script type="text/javascript">

var gvMap;
var gvMarkers = new Array();

function initialize() {
	  var myLatlng = new google.maps.LatLng(50.061781,19.937242);
	  var mapOptions = {
	    zoom: 15,
	    center: myLatlng
	  }
	  gvMap = new google.maps.Map(document.getElementById('googleMapDiv'), mapOptions);
	  
	  setTimeout(refreshMap, 10000);
}

function refreshMap() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			var responseStr = xmlhttp.responseText;
			var responseObj = JSON.parse(responseStr);
			if (responseObj.status == 'OK')
			{
				var drivers = responseObj.drivers;
				for (var i = 0; i < drivers.length; i++)
				{
					var driver = gvMarkers['t'+drivers[i].driverId];
					if (driver == 'undefined' || driver == null)
					{
						driver = new google.maps.Marker({
							position: new google.maps.LatLng(drivers[i].coordinates[0],drivers[i].coordinates[1]),
							map: gvMap,
							title: 'Taxi nr ' + drivers[i].driverId,
							icon: 'img/taxi-24.png'
						});
						gvMarkers['t' + drivers[i].driverId] = driver;
					}
					else
					{
						driver.setPosition(new google.maps.LatLng(drivers[i].coordinates[0],drivers[i].coordinates[1]));
					}
				}
				
				var clients = responseObj.clients;
				for (var i = 0; i < clients.length; i++)
				{
					var client = gvMarkers['c' + clients[i].id];
					if (client == 'undefined' || client == null)
					{
						client = new google.maps.Marker({
							position: new google.maps.LatLng(clients[i].coordinates[0],clients[i].coordinates[1]),
							map: gvMap,
							title: '',
							icon: 'img/street-view-24.png'
						});
					}
					else
					{
						client.setPosition(new google.maps.LatLng(clients[i].coordinates[0],clients[i].coordinates[1]));
					}
				}
			}
			setTimeout(refreshMap, 10000);
		}
	};
	xmlhttp.open("POST","currentData",true);
	xmlhttp.send();
}


google.maps.event.addDomListener(window, 'load', initialize);



</script>