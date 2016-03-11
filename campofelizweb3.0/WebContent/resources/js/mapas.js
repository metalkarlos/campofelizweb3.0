/*
Mapas en Google de a oficina y el camposanto de Campo Feliz
*/
    //Document ready function
    $(document).ready(function() {	
		//Google Map Oficina
		var mapCanvas = document.getElementById('map-canvas');
		if (typeof mapCanvas !== 'undefined' && mapCanvas !== null){
			var myLatlng = new google.maps.LatLng(-2.1431607,-79.9222974);
			var mapOptions = {
			zoom: 16,
			scrollwheel: true,
			center: myLatlng,
			mapTypeId: google.maps.MapTypeId.ROADMAP, //SATELLITE,
			// How you would like to style the map. 
	        styles: [{"featureType":"administrative","elementType":"all","stylers":[{"visibility":"on"},{"saturation":-100},{"lightness":20}]},{"featureType":"road","elementType":"all","stylers":[{"visibility":"on"},{"saturation":-100},{"lightness":40}]},{"featureType":"water","elementType":"all","stylers":[{"visibility":"on"},{"saturation":-10},{"lightness":30}]},{"featureType":"landscape.man_made","elementType":"all","stylers":[{"visibility":"simplified"},{"saturation":-60},{"lightness":10}]},{"featureType":"landscape.natural","elementType":"all","stylers":[{"visibility":"simplified"},{"saturation":-60},{"lightness":60}]},{"featureType":"poi","elementType":"all","stylers":[{"visibility":"off"},{"saturation":-100},{"lightness":60}]},{"featureType":"transit","elementType":"all","stylers":[{"visibility":"off"},{"saturation":-100},{"lightness":60}]}]
			}
			var mapIcon = document.getElementById('mapIcon').value;
			if (typeof mapIcon !== 'undefined' && mapIcon !== null ){
				var map = new google.maps.Map(mapCanvas, mapOptions)
				var marker = new google.maps.Marker({
				position: myLatlng,
				icon: mapIcon,
				map: map,
				title: 'Información Aquí!'
				});
			}
		}
		//Google Map Campo Santo
        
		var mapCanvasCampo = document.getElementById('map-canvas-campo');
		if (typeof mapCanvasCampo !== 'undefined' && mapCanvasCampo !== null){
			var myLatlngCampo = new google.maps.LatLng(-2.0363956,-79.965286);
			var mapOptionsCampo = {
			zoom: 16,
			scrollwheel: true,
			center: myLatlngCampo,
			mapTypeId: google.maps.MapTypeId.ROADMAP, //SATELLITE,
			// How you would like to style the map. 
	        styles: [{"featureType":"administrative","elementType":"all","stylers":[{"visibility":"on"},{"saturation":-100},{"lightness":20}]},{"featureType":"road","elementType":"all","stylers":[{"visibility":"on"},{"saturation":-100},{"lightness":40}]},{"featureType":"water","elementType":"all","stylers":[{"visibility":"on"},{"saturation":-10},{"lightness":30}]},{"featureType":"landscape.man_made","elementType":"all","stylers":[{"visibility":"simplified"},{"saturation":-60},{"lightness":10}]},{"featureType":"landscape.natural","elementType":"all","stylers":[{"visibility":"simplified"},{"saturation":-60},{"lightness":60}]},{"featureType":"poi","elementType":"all","stylers":[{"visibility":"off"},{"saturation":-100},{"lightness":60}]},{"featureType":"transit","elementType":"all","stylers":[{"visibility":"off"},{"saturation":-100},{"lightness":60}]}]
			}
			var mapCampo = new google.maps.Map(mapCanvasCampo, mapOptionsCampo)
			var markerCampo = new google.maps.Marker({
			position: myLatlngCampo,
			icon: mapIcon,
			map: mapCampo,
			title: 'Este es mi campo feliz!'
			});
		}	
       
    });
    
    
