/**
 * Asigna la etiqueta seleccionada de una lista a un tag oculto
 * @param selectedId Id de la lista 
 * @param hiddenId Id del tag oculto
 */
function setSelectedToHidden(selectedId, hiddenId){
	var selectedindex = document.getElementById(selectedId).selectedIndex;
	var selectedtext = document.getElementById(selectedId).options[selectedindex].text;
	document.getElementById(hiddenId).value = selectedtext;
}

/**
 * Función para el componente datepicker de JQuery que retorna fechas con estilo personalizado
 * para los cual debe existir un estilo definido con el nombre 'special_day'
 * @param date Fecha del datepicker antes del render
 * @param specialDates String con fechas agendadas separadas por comas
 * @returns {Array} Retorna true/false y el estilo de la fecha especial
 */
function specialDateStyle(date, specialDates) {
	var arrSpecialDates = specialDates.split(",");
	
	for(var i=0; i < arrSpecialDates.length; i++){
		if(date.getUTCDate() == arrSpecialDates[i]){
			return [true, 'special_day'];
		}
	}

	return [true, ''];
}

function redirect(url){
	window.location = url;
}

function paginaAnterior(){
	history.back();
}

function mensajeConfirmacion(mensaje){
	var resp = confirm(mensaje);
	return resp;
}

function not_internetexplorer(){
	if ((navigator.userAgent.search("MSIE") < 0) && (navigator.userAgent.search("Trident") < 0)){
		return true;
	}else{
		return false;
	}
}

/*$(function(){
	  $('form').on('keypress', function(event){
	    if(event.which === 13 && $(event.target).is(':input')){
	        event.preventDefault();
	        return false;
	    }
	  });
	});*/