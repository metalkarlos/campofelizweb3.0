$(document).ready(function() {
	//remueve todas las clases del boton primefaces
	$("#submit_btn").removeClass();
	
	//agrega las nuevas clases al boton
	$("#submit_btn").addClass("btn btn-default pull-right");
	
    //reset previously set border colors and hide all message on .keyup()
    $("#contact_form  input[required=true], #contact_form textarea[required=true]").keyup(function() { 
        $(this).css('background-color',''); 
        $("#contact_results").slideUp();
    });
});

function validarContacto() {
    var proceed = true;
    var input;
    //simple validation at client's end
    //loop through each field and we simply change border color to red for invalid fields
	$("#contact_form input[required], #contact_form textarea[required]").each(function(){
		$(this).css('background-color','');
		//if this field is empty
		if(!$.trim($(this).val())){  
			$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE
			input = input?input:this; 
			
			proceed = false; //set do not proceed flag
		}
		//check invalid email
		var email_reg = /^([\w\.-]*[a-zA-Z0-9_]@[\w\.-]*[a-zA-Z0-9]\.[a-zA-Z][a-zA-Z\.]*[a-zA-Z])?$/; 
		if($(this).attr("type")=="email" && !email_reg.test($.trim($(this).val()))){
			$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE
			this.setCustomValidity("Formato incorrecto. Ej email@empresa.com");
			input = input?input:this;
			
			proceed = false; //set do not proceed flag				
		}
		//check invalid Id
		var identity_reg = /^([0-9]{10})?$/;
		if($(this).attr("type")=="identity" && !identity_reg.test($.trim($(this).val()))){
			$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE
			this.setCustomValidity("Numero de cedula invalido. Ej 0123456789");
			input = input?input:this;
			
			proceed = false; //set do not proceed flag				
		}
		//check invalid phone
		var phone_reg = /^([0-9][0-9\s]+[0-9])?$/;
		if($(this).attr("type")=="phone" && !phone_reg.test($.trim($(this).val()))){
			$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE
			this.setCustomValidity("Numero de telefono invalido. Ej 042123456");
			input = input?input:this;
			
			proceed = false; //set do not proceed flag				
		}
	});
	
	//verify email
	if($("#email").val() != $("#email2").val()){
		$("#email2").css('background-color','#FFDEDE'); //change border color to #FFDEDE
		$("#email2")[0].setCustomValidity("Correo no coincide con el especificado");
		input = input?input:$("#email2")[0];
		
		proceed = false; //set do not proceed flag
	}
	
	if(input){
		//document.getElementById("contact_results").innerHTML = "<div class=\"danger\"><div class=\"alert alert-danger\" role=\"alert\"><p>"+input.validationMessage+"</p></div></div><br>";
		$(input).focus();
	}
	
	return proceed;
}

