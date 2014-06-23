var xmlHttp;// global instance of XMLHttpRequest
var depChoisiTan="", arrChoisiTan="";


function reqServ(quoi){
	var dep, arr;
	//on recupere le depart et l'arrivee
	dep=document.getElementById("dep").value;
	arr=document.getElementById("arr").value;

	if(quoi=="bus" && dep!="" && arr!=""){
		reqPosTan("arr");
	}
	else{
		if(quoi!="bus")
			document.getElementById('loupe').className=('loupeVis');
		//on convertie en coordonnees gps
		var geocoder = new google.maps.Geocoder();
		var url;
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
			url=chemin+"ajax?&quoi="+quoi+"&dep="+tmp[0]+"&arr="+tmp[1];
			xmlHttp.open("GET", url);
			xmlHttp.onreadystatechange=handleStateChange;
			xmlHttp.send(null);
		});
	}
}


function affAdresse(repServ){
	if(repServ.indexOf("adresse plus precise")> -1){
		alert(repServ.split(';')[1]);
		if(repServ.split(';')[2]=="dep")
			depChoisiTan="noResult";
		else
			arrChoisiTan="noResult";
	}
	else{
		var rep=repServ.split(';');
		var qui= rep[rep.length-1];
		var resHTML="<p><h2>Veuillez saisir votre ";
		if(qui=="dep")
			resHTML+="point de depart</h2>";
		else
			resHTML+="point d'arrivee</h2>";
		
		for(var i=1;i<rep.length-1;i++){
			var a = rep[i].split('[');
			resHTML+="<input type='radio' onclick=\"choisir('"+a[1]+"','"+qui+"')\"/>"+utf8_decode(a[0])+"<br/>";
		}
		
		resHTML+="<input type='button' value='Annuler' onclick=\"annuler('"+qui+"')\"/></p>";
		document.getElementById("choixPointTan"+qui).innerHTML=resHTML;
	}

	if(qui=="arr"){
		reqPosTan("dep");
		document.getElementById("choixPointTandep").className="choixVis";
		document.getElementById("choixPointTanarr").className="choixVis";
	}
		
}

function choisir(val,qui){
	if(qui.indexOf("choixIti:")> -1){
		createXmlHttpRequest();
		url=chemin+"ajax?&quoi=choixIti&iti="+qui.split(":")[1];
		xmlHttp.open("GET", url);
		xmlHttp.onreadystatechange=handleStateChange;
		xmlHttp.send(null);
		document.getElementById("choixItisTan").className="choixInvis";
		document.getElementById("choixItisTan").innerHTML="";
		depChoisiTan="";
		arrChoisiTan="";
	}
	else{
		document.getElementById("choixPointTan"+qui).innerHTML="";
		document.getElementById("choixPointTan"+qui).className="choixInvis";
		if(qui=="dep"){
			depChoisiTan=val;
		}
		else{
			arrChoisiTan=val;
		}
		if(depChoisiTan!="" && arrChoisiTan!="" && depChoisiTan!="noResults" && arrChoisiTan!="noResults"){
			createXmlHttpRequest();
			url=chemin+"ajax?&quoi=bus&dep="+depChoisiTan+"&arr="+arrChoisiTan;
			xmlHttp.open("GET", url);
			xmlHttp.onreadystatechange=handleStateChange;
			xmlHttp.send(null);
		}
	}
}

function annuler(qui){
	if(qui=="iti"){
		document.getElementById("choixItisTan").className="choixInvis";
		document.getElementById("choixItisTan").innerHTML="";
	}
	else{
	document.getElementById("choixPointTan"+qui).className="choixInvis";
	document.getElementById("choixPointTan"+qui).innerHTML="";
	if(qui=="dep")
		depChoisiTan="";
	if(qui=="arr")
		arrChoisiTan="";
	}
}

function handleStateChange()
{
    if(xmlHttp.readyState==4)
    {
        if(xmlHttp.status==200)
            {
          var message = xmlHttp.responseText;
          var res=message.split(';');
          if(res[0]=="bicloo"){
        	  placeMarker(parseFloat(res[1].split(',')[0]), parseFloat(res[1].split(',')[1]))
        	  placeMarker(parseFloat(res[2].split(',')[0]), parseFloat(res[1].split(',')[1]))
          }
          if(res[0]=="bus"){
        	  affItis(message.substring(3,message.length));
          }
          if(res[0]=="adresse"){
        	  affAdresse(message);
          }
          if(res[0]=="iti"){
        	  multiChemin(message.substring(4,message.length));
          }
          //alert(message);

             //document.getElementById("results").innerHTML=message;
            }
        else
        {
           alert("Error loading pagen"+ xmlHttp.status +":"+xmlHttp.statusText);
        }
    }
}

function affItis(repServ){
	var popup=document.getElementById("choixItisTan");
	var rep=repServ.split(';');
	var resHTML="<p><h2>Veuillez saisir votre itineraire</h2>";
	
	for(var i=1;i<rep.length-1;i++){
		var a = rep[i].split('/')[0].split(',');
		resHTML+="<input type='radio' onclick=\"choisir('"+a[1]+"','"+"choixIti:"+i+"')\"/>"+utf8_decode(a[0])+"<br/>";
		var etapes = rep[i].split('/')[1].split(',');
		for(j=0;j<etapes.length;j++){
			resHTML+="<ul>"+etapes[j]+"</ul>";
		}
	}
	
	resHTML+="<input type='button' value='Annuler' onclick=\"annuler('iti')\"/></p>";
	popup.innerHTML=resHTML;
	document.getElementById("choixItisTan").className="choixVis";
	arrChoisiTan="";
	depChoisiTan="";
}

function createXmlHttpRequest()
{
       if(window.ActiveXObject)
       {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
      }

    else if(window.XMLHttpRequest)
    {
        xmlHttp=new XMLHttpRequest();
     }
}

function reqPosTan(qui){
	createXmlHttpRequest();
	var elem,ou="";
	if(qui=='dep' || qui=='arr'){
		elem=document.getElementById(qui);
		ou = replaceAll(' ','-',elem.value);
	}
	url=chemin+"ajax?quoi=adresse&ou="+ou+"&qui="+qui;
	xmlHttp.open("GET", url);
	xmlHttp.onreadystatechange=handleStateChange;
	xmlHttp.send(null);
}
