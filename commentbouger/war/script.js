var map;
var directionsDisplay;
var directionsService;
var stepDisplay;
var markerArray = [];

function initialize() {
  // Instantiate a directions service.
  directionsService = new google.maps.DirectionsService();

  // Create a map and center it on Manhattan.
  var manhattan = new google.maps.LatLng(47.2092934, -1.5476554);
  var mapOptions = {
    zoom: 13,
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
      travelMode: google.maps.TravelMode.DRIVING
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

google.maps.event.addDomListener(window, 'load', initialize);




/*



function initialize() {
  	var posInit
    var mapOptions = {
      center: new google.maps.LatLng(47.2092934, -1.5476554),
      zoom: 12
    };
    var map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);
  }
google.maps.event.addDomListener(window, 'load', initialize);

function changer(){
	var map = document.getElementById("map-canvas");
	
}

function calcRoute() {
  var start = document.getElementById("dep").value;
  var end = document.getElementById("arr").value;
  var request = {
    origin:start,
    destination:end,
    travelMode: google.maps.TravelMode.DRIVING
  };
  directionsService.route(request, function(result, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      directionsDisplay.setDirections(result);
    }
  });
}

/*
var src1 = 'https://www.google.com/maps/embed/v1/directions?key=AIzaSyCty_tfQgPAUaz8zjBc9ckJ6oFfUH9EJlg&origin=';
var src2 = '&destination=';
var src3 = '&mode=walking';

function changer(){
	var map = document.getElementById('map');
	var dep = document.getElementById('dep').value;
	var arr = document.getElementById('arr').value;
	var auto = document.getElementById('autoroute').checked;
	var peage= document.getElementById('peage').checked;
	var src4 = "";
	if(peage==true)
		src4="&avoid=tolls";
	if(auto==true){
		if(src4=="")
			src4="&avoid=highways";
		else
			src4+="|highways";
	}
	var nouvSrc = src1 + dep + src2 + arr + src4 + src3;
	map.src=nouvSrc;
	
}*/