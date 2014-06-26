//variable local ou deployee :
//var chemin = "http://1-dot-nantes-commentbouger.appspot.com/";
//var chemin = "http://localhost:8888/";
var chemin=document.URL;
var tempBicloo=0;
var distBic=0;
var bicloo=true;
var cntBicloo=0;
var map;
var directionsDisplay;
var directionsService;
var stepDisplay;
var markerArray = [];
var voiture = document.getElementById("voiture");
var bus = document.getElementById("bus");
var velo = document.getElementById("velo");
var bicloo = document.getElementById("bicloo");
var pied = document.getElementById("pied");
var nb;
var centreNantes = new google.maps.LatLng(47.20731701,-1.54258325);


//tableau de LatLng : liste des étapes
var polyRoute =[];
var tempRoute = [];

function coordFromAdress(adress){
	var coord = new google.maps.LatLng;
	var request = {
			origin: start
	};

}


function initialize() {
	nb=0;
	// Instantiate a directions service.
	directionsService = new google.maps.DirectionsService();

	// Create a map and center it on Nantes.

	var mapOptions = {
			zoom: 12,	
			center: centreNantes
	}
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

	// Create a renderer for directions and bind it to the map.
	var rendererOptions = {
			map: map
	}
	directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions)

	// Instantiate an info window to hold step text.
	stepDisplay = new google.maps.InfoWindow();
	//-1.5563652,47.2134985,checkPied;47.21694925,-1.55679602,checkBus;47.22998380,-1.61681022,checkBus
	//-1.5563652,47.2134985,checkPied;-1.55679602,47.21694925,checkBus;-1.61681022,47.22998380,checkBus
	//multiChemin("47.22019369,-1.60338192,checkPied;47.25366765,-1.59771330,checkTram;47.15931812,-1.59829606,checkTram;47.15685237,-1.59744147,checkTram;47.22973235,-1.58938102,checkBicloo;47.26978758,-1.57207782,checkVoiture;47.21409134,-1.72340027,checkBus;47.20911263,-1.55025692,checkVelo;47.22019369,-1.60338192,checkPied");
	//multiChemin("47.2134985,-1.5563652,checkPied;47.21694925,-1.55679602,checkBus;47.22998380,-1.61681022,checkBus");
}

function changerApparence(quoi){
	var elems = [document.getElementById("checkVoiture"),document.getElementById("checkBus"),document.getElementById("checkVelo"),document.getElementById("checkBicloo"),document.getElementById("checkPied")];
	var i=0;

	var start = document.getElementById('dep').value;
	var end = document.getElementById('arr').value;
	if(end !="" && start!=""){
		for(i=0;i<elems.length;i++){
			if(elems[i].id==quoi && elems[i].border!="3"){
				nb+=1;
				elems[i].border="3";


				var affRes = document.getElementById("bandeauBas");
				var mode, modeFR;
				if(elems[i].id=="checkVoiture"){mode = google.maps.TravelMode.DRIVING;modeFR="Voiture";}
				if(elems[i].id=="checkVelo"){mode = google.maps.TravelMode.BICYCLING;modeFR="Velo";}
				if(elems[i].id=="checkPied"){mode = google.maps.TravelMode.WALKING;modeFR="A pied";}

				if(nb>0){
					var duree;
					var dist;
					var request = {
							origin: start,
							destination: end,
							travelMode: mode
					};
					directionsService.route(request, function(response, status) {
						if (status == google.maps.DirectionsStatus.OK) {
							/*var warnings = document.getElementById('warnings_panel');
					      warnings.innerHTML = '<b>' + response.routes[0].warnings + '</b>';*/
							directionsDisplay.setDirections(response);
							showSteps(response,"non");
						}
					});
				}
			}
			else if(elems[i].id==quoi && elems[i].border=="3"){
				nb-=1;
				elems[i].border="0";
			}
			else if(elems[i].border=="3"){
				nb-=1;
				elems[i].border="0";
			}
			else{
				elems[i].border="0";
			}
		}	

		if(nb>0){
			document.getElementById("bandeauBas").style.height="250px";
			document.getElementById("bandeauBas").style.top="85%";
			document.getElementById("bandeauBas").style.opacity="1";
		}
		else{
			document.getElementById("bandeauBas").style.height="0%";
			document.getElementById("bandeauBas").style.top="100%";
			document.getElementById("bandeauBas").style.opacity="0";
			directionsDisplay.setDirections({routes: []});
		}

	}
	majTempsTableau(start, end);
}

function majTempsTableau(start, end){
	var mode= [google.maps.TravelMode.DRIVING, google.maps.TravelMode.BICYCLING, google.maps.TravelMode.WALKING];
	var affRes = document.getElementById("affCompare");
	affRes.innerHTML=("<table class=\"table table-striped\"><tr><th>Mode</th><th>Distance</th><th>Duree</th><th>Prix</th></tr>");
	var duree;
	var dist;
	if(bicloo==true){
		affRes.innerHTML=affRes.innerHTML.substring(0, affRes.innerHTML.length-16)+"<tr><td>Bicloo</td><td>"+distBic+" km</td><td>"+tempBicloo+" min</td><td>1€</td></tr></table>";
	}
	for(var i=0;i<3;i++){
		var request = {
				origin: start,
				destination: end,
				travelMode: mode[i]
		};
		directionsService.route(request, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				/*var warnings = document.getElementById('warnings_panel');
		      warnings.innerHTML = '<b>' + response.routes[0].warnings + '</b>';*/
				var modeFR;
				if(response.Pb.travelMode=="DRIVING"){modeFR="Voiture";}
				if(response.Pb.travelMode=="BICYCLING"){modeFR="Velo";}
				if(response.Pb.travelMode=="WALKING"){modeFR="A pied";}
				var prix;
				dist = response.routes[0].legs[0].distance.text;
				duree= response.routes[0].legs[0].duration.text;
				if(modeFR=="Voiture"){
					prix=dist.split(" ")[0]/100*6*1,5;
					prix=Math.round(prix*100)/100+"€";
				}
				if(modeFR=="Velo")
					prix="De la sueur";
				if(modeFR=="A pied")
					prix="Du courage";
				
				affRes.innerHTML=affRes.innerHTML.substring(0, affRes.innerHTML.length-8)+("<tr><td>"+modeFR+"</td><td>"+dist+"</td><td>"+duree+"</td><td>"+prix+"</td></tr></table>");
				if(bicloo==true){
					distBic=0;
					tempBicloo=0;
					bicloo=0;
					cntBicloo=0;
				}
			}
		});
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
	if(1){
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

}

function majTabTanBic(quoi){
	var start,end;
	dep=document.getElementById("dep").value;
	arr=document.getElementById("arr").value;
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({
	    "address": dep
	}, function(results) {
		document.getElementById("cache").innerHTML=results[0].geometry.location.lat()+","+results[0].geometry.location.lng();
	});
	
	geocoder.geocode({
	    "address": arr
	}, arr=function(results) {
		document.getElementById("cache").innerHTML+=";"+results[0].geometry.location.lat()+","+results[0].geometry.location.lng();
		createXmlHttpRequest();
		var tmp;
		tmp=document.getElementById("cache").innerHTML.split(';');
		majTempsTableau(tmp[0], tmp[1])
	});
}

function attachInstructionText(marker, text) {
	google.maps.event.addListener(marker, 'click', function() {
		// Open an info window when the marker is clicked on,
		// containing the text of the step.
		stepDisplay.setContent(text);
		stepDisplay.open(map, marker);
	});
}

-google.maps.event.addDomListener(window, 'load', initialize);


///////////////
//MODIF CAF //
///////////////
//fonction qui place un marker et eventuellement une infobulle
//parametres : google latlng et libelle de l'infobulle
function placeMarker(latlng, libelle,mode){
var image;
if (mode == "arrivee"){
image = "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
}
else if(debut){
image = "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
}
else{
image = "http://maps.google.com/mapfiles/ms/icons/blue-dot.png";
}
var marker = new google.maps.Marker({
position: latlng,
map: map,
icon:image
});
if (libelle != null && libelle != "" && libelle != "undefined"){
var myWindowOptions = {
	content:'<p>' + libelle + '</p>'
};
var myInfoWindow = new google.maps.InfoWindow(myWindowOptions);
myInfoWindow.open(map,marker);
// Affichage de la fenêtre au click sur le marker
google.maps.event.addListener(marker, 'click', function() {
	myInfoWindow.open(map,marker);
});
}
}

///////////////
//MODIF CAF //
///////////////
//fonction qui affiche une polyline (pour les tram)
//parametre : myRoute string de coordonnees "lat,lng;lat,lng;lat,lng"
function affPolyline(myRoute){
//on recupere tous les points
var tempRoute = myRoute.split(";");
//tableau de LatLng : liste des points de la polyline
var polyRoute =[];
for (var i = 1; i < tempRoute.length; i++) {
var tempPoint = tempRoute[i].split(",");
polyRoute.push(new google.maps.LatLng( parseFloat(tempPoint[0]), parseFloat(tempPoint[1]) ));
if (i==1){
//on mets un marqueur sur le départ du trajet
placeMarker(new google.maps.LatLng( parseFloat(tempPoint[0]), parseFloat(tempPoint[1]) ),tempPoint[3],"");
debut = false;
}
else if (tempPoint[3] != null && tempPoint[3] != "" && tempPoint[3] != "undefined"){
placeMarker(new google.maps.LatLng( parseFloat(tempPoint[0]), parseFloat(tempPoint[1]) ),tempPoint[3],"");
}
}
//options de la polyline : nom de la map et du tableau de LatLng
var optionPoly = {
map:map,
path:polyRoute,
strokeColor: "#00CC00" ,
strokeOpacity: 0.8,
strokeWeight: 5};
//creation de la polyline
var myPolyline = new google.maps.Polyline(optionPoly);
stepDisplay.open(map, myPolyline);
traceRoute();
}
///////////////

///////////////
//MODIF CAF //
///////////////
function traceRoute(){
if (tempRoute.length > 1){
var tempPoint = tempRoute.shift().split(",");
//on teste le mode de transport : si tram -> polyline, sinon trajet normal
if (tempPoint[2] == "checkTram" || tempPoint[2] == "checkBus"){
var myRoute = "";
do{
	myRoute = myRoute + ";" + + tempPoint[0] + "," + tempPoint[1] + "," + tempPoint[2] + "," + tempPoint[3];
	var tempPoint = tempRoute.shift().split(",");
}while((tempPoint[2] == "checkTram" || tempPoint[2] == "checkBus") && tempRoute.length > 0);
myRoute = myRoute + ";" + + tempPoint[0] + "," + tempPoint[1];
var temp = tempPoint[0] + "," + tempPoint[1] + "," + tempPoint[2] + "," + tempPoint[3];
tempRoute.unshift(temp);
affPolyline(myRoute);
}
else{
//on définit le point de départ de l'étape
var start = new google.maps.LatLng( parseFloat(tempPoint[0]), parseFloat(tempPoint[1]) );
//on mets un marqueur sur le départ du trajet
placeMarker(new google.maps.LatLng( parseFloat(tempPoint[0]), parseFloat(tempPoint[1]) ),tempPoint[3],"");
debut = false;
var mode;
//on définit le mode de transport et la couleur du trajet (voiture:rouge / bus:vert / bicloo:orange / vélo:violet / pied:bleu)
switch (tempPoint[2]){
case "checkVoiture":
	mode = google.maps.TravelMode.DRIVING;
	modeFR="Voiture";
	var option = new google.maps.Polyline({
		strokeColor : "#FF0000",
		strokeOpacity : 0.8,
		strokeWeight : 5,
		map : map
	});
	break;
case "checkBicloo":
	mode = google.maps.TravelMode.BICYCLING;
	modeFR="Velo";
	var option = new google.maps.Polyline({
		strokeColor : "#FF6600",
		strokeOpacity : 0.8,
		strokeWeight : 5,
		map : map
	});
	break;
case "checkVelo":
	mode = google.maps.TravelMode.BICYCLING;
	modeFR="Velo";
	var option = new google.maps.Polyline({
		strokeColor : "#CC33FF",
		strokeOpacity : 0.8,
		strokeWeight : 5,
		map : map
	});
	break;
case "checkPied":
	mode = google.maps.TravelMode.WALKING;
	modeFR="A pied";
	var option = new google.maps.Polyline({
		strokeColor : "#0066FF",
		strokeOpacity : 0.8,
		strokeWeight : 5,
		map : map
	});
	break;
}
//on définit le point d'arrivée
tempPoint = tempRoute[0].split(",");
var end = new google.maps.LatLng( parseFloat(tempPoint[0]), parseFloat(tempPoint[1]) );
var request = {
	origin:start,
	destination:end,
	travelMode: mode
};
directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true,polylineOptions:option});
directionsDisplay.setMap(map);
//on affiche le trajet
directionsService.route(request, function(result, status) {
	if (status == google.maps.DirectionsStatus.OK) {
		if(bicloo==true){
			cntBicloo+=1;
			var tmp=result.routes[0].legs[0].duration.text;
			tmp=tmp.split(" ")[0];
			if(tmp!="Undefined")
				tempBicloo+=parseInt(tmp);
			tmp=result.routes[0].legs[0].distance.text;
			tmp=tmp.split(" ")[0];//.split(".")[0];
			if(tmp!="Undefined")
				distBic+=parseFloat(tmp);
			if(cntBicloo==3)
				majTabTanBic("bicloo");
			
		}
		directionsDisplay.setDirections(result);
		traceRoute();
	}
});
}	
}
else{
//on place un marqueur sur l'arrivee
tempPoint = tempRoute[0].split(",");
placeMarker(new google.maps.LatLng( parseFloat(tempPoint[0]), parseFloat(tempPoint[1]) ),tempPoint[3],"arrivee");
}
}

function centreMap(){
var zoneMarqueurs = new google.maps.LatLngBounds(new google.maps.LatLng(47.156458, -1.688747),new google.maps.LatLng(47.293787, -1.428852));
map.fitBounds(zoneMarqueurs);
}

///////////////

///////////////
//MODIF CAF //
///////////////
//fonction qui affiche plusieurs chemins a la suite
function multiChemin(myRoute){
//on efface tous les anciens itinéraires
var manhattan = new google.maps.LatLng(47.19631701,-1.54258325);
mapOptions = {
zoom: 18,	
center: manhattan
}
map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
//on recupere tous les points
tempRoute = myRoute.split(";");
debut = true;
traceRoute();
}
///////////////


// boite de dialogue : 
$(function() {
    $( "#choixPointTandep" ).dialog({
      autoOpen: false,
      show: {
        effect: "size",
        duration: 400
      },
      hide: {
        effect: "size",
        duration: 400
      }
    });
 
    $( "#checkBus" ).click(function() {
      $( "#choixPointTandep" ).dialog( "open" );
    });
  });

$(function() {
    $( "#choixPointTanarr" ).dialog({
      autoOpen: false,
      show: {
        effect: "size",
        duration: 400
      },
      hide: {
        effect: "size",
        duration: 400
      }
    });
 
    $( "#checkBus" ).click(function() {
      $( "#choixPointTanarr" ).dialog( "open" );
    });
  });

$(function() {
    $( "#dialog" ).dialog({
      autoOpen: true,
      dialogClass: "no-close",
      resizable: false,
      position: { my: "left top", at: "left bottom", of: window },
      width: 300,
      height: 160,
      show: {
        effect: "size",
        duration: 400
      },
      hide: {
        effect: "size",
        duration: 400
      }
    });
  });

$(function() {
    $( "#affCompare" ).dialog({
      autoOpen: false,
      position: { my: "left middle", at: "left middle", of: window },
      width: 380,
      height: 250,
      show: {
        effect: "size",
        duration: 400
      },
      hide: {
        effect: "size",
        duration: 400
      }
    });
  });

$(function() {
    $( "#choixItisTan" ).dialog({
      autoOpen: false,
      show: {
        effect: "size",
        duration: 400
      },
      hide: {
        effect: "size",
        duration: 400
      }
    });
  });

