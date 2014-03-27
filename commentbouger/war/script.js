var src1 = 'https://www.google.com/maps/embed/v1/directions?key=AIzaSyCty_tfQgPAUaz8zjBc9ckJ6oFfUH9EJlg&origin=';
var src2 = '&destination=';
var src3 = '&mode=driving';

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
	
}