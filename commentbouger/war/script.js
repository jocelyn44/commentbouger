var map;
var directionsDisplay;
var directionsService;
var stepDisplay;
var markerArray = [];

function coordFromAdress(adress){
	var coord = new google.maps.LatLng;
	var request = {
		      origin: start
		  };

}

function initialize() {
  // Instantiate a directions service.
  directionsService = new google.maps.DirectionsService();

  // Create a map and center it on Nantes.
  //var manhattan = new google.maps.LatLng(47.218371, -1.553621);
  var manhattan = new google.maps.LatLng(47.19631701,-1.54258325);
  
  var mapOptions = {
    zoom: 18,	
    center: manhattan
  }
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

  // Create a renderer for directions and bind it to the map.
  var rendererOptions = {
    map: map
  }
  directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions)

  // Instantiate an info window to hold step text.
  stepDisplay = new google.maps.InfoWindow();
}

function changerEtat(){
	var nbTrue=0;
	if(document.getElementById("voiture").checked==true)
		nbTrue+=1;
	if(document.getElementById("bus").checked==true)
		nbTrue+=1;
	if(document.getElementById("velo").checked==true)
		nbTrue+=1;
	if(document.getElementById("bicloo").checked==true)
		nbTrue+=1;
	if(document.getElementById("pied").checked==true)
		nbTrue+=1;
	if(nbTrue>0){
		document.getElementById("bandeauBas").style.height="10%";
		document.getElementById("bandeauBas").style.top="90%";
		document.getElementById("bandeauBas").style.opacity="1";
	}
	else{
		document.getElementById("bandeauBas").style.height="0%";
		document.getElementById("bandeauBas").style.top="100%";
		document.getElementById("bandeauBas").style.opacity="0";
	}

}

function calcRoute() {

  // First, remove any existing markers from the map.
  for (var i = 0; i < markerArray.length; i++) {
    markerArray[i].setMap(null);
  }

  // Now, clear the array itself.
  markerArray = [];

  // Retrieve the start and end locations and create
  // a DirectionsRequest using WALKING directions.
  var start = document.getElementById('dep').value;
  var end = document.getElementById('arr').value;
  var request = {
      origin: start,
      destination: end,
      travelMode: google.maps.TravelMode.DRIVING,
      map: map
  };

  // Route the directions and pass the response to a
  // function to create markers for each step.
  directionsService.route(request, function(response, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      var warnings = document.getElementById('warnings_panel');
      warnings.innerHTML = '<b>' + response.routes[0].warnings + '</b>';
      directionsDisplay.setDirections(response);
      showSteps(response,"non");
    }
  });
}

function showSteps(directionResult,affAll) {
  // For each step, place a marker, and add the text to the marker's
  // info window. Also attach the marker to an array so we
  // can keep track of it and remove it when calculating new
  // routes.
  var myRoute = directionResult.routes[0].legs[0];
  if(affAll=="non"){
	  for (var i = 0; i < myRoute.steps.length; i++) {
	    if(i==0 && i==myRoute.steps.length-1){
			  var marker = new google.maps.Marker({
		      position: myRoute.steps[i].start_location,
		      map: map
		    });
		    attachInstructionText(marker, myRoute.steps[i].instructions);
		    markerArray[i] = marker;
	    }
	  }
  }
  document.getElementById("tmpVoiture").innerText=myRoute.duration.text;
}

function attachInstructionText(marker, text) {
  google.maps.event.addListener(marker, 'click', function() {
    // Open an info window when the marker is clicked on,
    // containing the text of the step.
    stepDisplay.setContent(text);
    stepDisplay.open(map, marker);
  });
}

function placeMarker(coordX, coordY){
	var loc = new google.maps.LatLng(coordX, coordY);
		  var marker = new google.maps.Marker({
		    position: loc,
		    map: map,
		  });
		  var infowindow = new google.maps.InfoWindow({
		    content: 'Latitude: ' + loc.lat() + '<br>Longitude: ' + loc.lng()
		  });
		  infowindow.open(map,marker);
}


google.maps.event.addDomListener(window, 'load', initialize);