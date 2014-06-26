var xmlHttp;// global instance of XMLHttpRequest
var depChoisiTan="", arrChoisiTan="";

function connexion(){
	createXmlHttpRequest();
	url=chemin+"loginservlet";
	xmlHttp.open("GET", url);
	xmlHttp.onreadystatechange=handleStateChange;
	xmlHttp.send(null);
	
}

function reqServ(quoi){
	var dep, arr;
	//on recupere le depart et l'arrivee
	dep=document.getElementById("dep").value;
	arr=document.getElementById("arr").value;

	if(quoi=="bus" && dep!="" && arr!=""){
		reqPosTan("arr");
		//affItis("47.2034979,-1.5507195,checkPied,Marcher jusqu'a l'arret Wattignies;47.20396028,-1.54645317,checkTram,Prendre la ligne 2 vers Orvault-Grand Val jusqu'a Place du Cirque;47.20913625,-1.55022427,checkTram;47.21457190,-1.55587749,checkTram;47.21696213,-1.55705473,checkTram;47.21676964,-1.55704733,checkBus,Prendre la ligne C6 vers Hermeland jusqu'a Ambroise Pare;47.22038744,-1.57017375,checkBus;47.22224693,-1.58490511,checkBus;47.22899173,-1.59789479,checkBus;47.22908658,-1.60855912,checkBus;47.2318918,-1.6092175,checkPied,Marcher jusqu'a l'arret Allee CLAUDE ROUGET DE LISLE");
	}
	else{
		$( "#affCompare" ).dialog( "open" );
		//on convertie en coordonnees gps
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
			if(quoi=="bicloo"){
				bicloo=true;
				cntBicloo=0;
				url=chemin+"bicloo?&dep="+tmp[0]+"&arr="+tmp[1];
				xmlHttp.open("GET", url);
				xmlHttp.onreadystatechange=handleStateChange;
				xmlHttp.send(null);
			}
			else{
				multiChemin(tmp[0]+","+quoi+";"+tmp[1]+","+quoi);
				majTempsTableau(tmp[0], tmp[1])
			}
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
		var resHTML="<p>Veuillez saisir votre ";
		if(qui=="dep")
			resHTML+="point de depart";
		else
			resHTML+="point d'arrivee";
		
		for(var i=1;i<rep.length-1;i++){
			var a = rep[i].split('[');
			resHTML+="</p><input type='radio' onclick=\"choisir('"+a[1]+"','"+qui+"')\"/>"+utf8_decode(a[0])+"<br/>";
		}
		
		//resHTML+="<input type='button' value='Annuler' onclick=\"annuler('"+qui+"')\"/></p>";
		document.getElementById("choixPointTan"+qui).innerHTML=resHTML;
	}

	if(qui=="arr"){
		reqPosTan("dep");
		//document.getElementById("choixPointTandep").className="choixVis";
		//document.getElementById("choixPointTanarr").className="choixVis";
	}
		
}

function choisir(val,qui){
	if(qui.indexOf("choixIti:")> -1){
		createXmlHttpRequest();
		url=chemin+"choixtan?&iti="+qui.split(":")[1];
		xmlHttp.open("GET", url);
		xmlHttp.onreadystatechange=handleStateChange;
		xmlHttp.send(null);
		document.getElementById("choixItisTan").className="";
		document.getElementById("choixItisTan").innerHTML="";
		depChoisiTan="";
		arrChoisiTan="";
		$( "#choixItisTan" ).dialog( "close" );
	}
	else{
		document.getElementById("choixPointTan"+qui).innerHTML="";
		document.getElementById("choixPointTan"+qui).className="";
		$( "#choixPointTan"+qui ).dialog( "close" );
		if(qui=="dep"){
			depChoisiTan=val;
		}
		else{
			arrChoisiTan=val;
		}
		if(depChoisiTan!="" && arrChoisiTan!="" && depChoisiTan!="noResults" && arrChoisiTan!="noResults"){
			createXmlHttpRequest();
			url=chemin+"ititan?&dep="+depChoisiTan+"&arr="+arrChoisiTan;
			xmlHttp.open("GET", url);
			xmlHttp.onreadystatechange=handleStateChange;
			xmlHttp.send(null);
		}
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
        	  multiChemin(message.substring(7,message.length));
				map.setCenter( centreNantes );
          }
          if(res[0]=="bus"){
        	  affItis(message.substring(4,message.length));
          }
          if(res[0]=="google"){
        	  affAdresse(message);
          }
          if(res[0]=="adresse"){
        	  affAdresse(message);
          }
          if(res[0]=="iti"){
        	  multiChemin(message.substring(4,message.length));
				map.setCenter( centreNantes );
          }
          if(res[0]=="erreur")
        	  alert(message.split(";")[1]);

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
	var resHTML="<p>";
	
	for(var i=1;i<rep.length-1;i++){
		var a = rep[i].split('/')[0].split(',');
		resHTML+="<input type='radio' onclick=\"choisir('"+a[1]+"','"+"choixIti:"+i+"')\"/>"+utf8_decode(a[0])+"<br/>";
		var etapes = rep[i].split('/')[1].split(',');
		for(j=0;j<etapes.length;j++){
			resHTML+="<ul>"+etapes[j]+"</ul>";
		}
	}
	
	resHTML+="</p>";
	popup.innerHTML=resHTML;
	$( "#choixItisTan" ).dialog( "open" );
	//document.getElementById("choixItisTan").className="choixVis";
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
	url=chemin+"adresse?&ou="+ou+"&qui="+qui;
	xmlHttp.open("GET", url);
	xmlHttp.onreadystatechange=handleStateChange;
	xmlHttp.send(null);
}
