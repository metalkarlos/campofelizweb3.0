$(document).ready(function() {
    $("#submit_btn").click(function() { 
       
	    var proceed = true;
        //simple validation at client's end
        //loop through each field and we simply change border color to red for invalid fields		
		$("#contact_form input[required], #contact_form textarea[required]").each(function(){
			$(this).css('background-color','');
			//if this field is empty
			if(!$.trim($(this).val())){  
				$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE   
				proceed = false; //set do not proceed flag
			}
			//check invalid email
			var email_reg = /^([\w\.-]*[a-zA-Z0-9_]@[\w\.-]*[a-zA-Z0-9]\.[a-zA-Z][a-zA-Z\.]*[a-zA-Z])?$/; 
			if($(this).attr("type")=="email" && !email_reg.test($.trim($(this).val()))){
				$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE   
				proceed = false; //set do not proceed flag				
			}
			//check invalid Id
			var identity_reg = /^([0-9]{10})?$/;
			if($(this).attr("type")=="identity" && !identity_reg.test($.trim($(this).val()))){
				$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE   
				proceed = false; //set do not proceed flag				
			}
			//check invalid phone
			var phone_reg = /^([0-9][0-9\s]+[0-9])?$/;
			if($(this).attr("type")=="phone" && !phone_reg.test($.trim($(this).val()))){
				$(this).css('background-color','#FFDEDE'); //change border color to #FFDEDE   
				proceed = false; //set do not proceed flag				
			}
		});
       
        if(proceed) //everything looks good! proceed...
        {
			//get input field values data to be sent to server
            post_data = {
				'user_name'		: $('input[name=name]').val(), 
				'user_email'	: $('input[name=email]').val(), 
				'subject'		: $('input[name=subject]').val(), 
				'msg'			: $('textarea[name=message]').val()
			};
            
            //Ajax post data to server
            $.post('php/sendmail.php', post_data, function(response){  
				if(response.type == 'error'){ //load json data from server and output message     
					output = '<div class="error">'+response.text+'</div><br>';
				}else{
				    output = '<div class="success">'+response.text+'</div><br>';
					//reset values in all input fields
					$("#contact_form  input[required=true], #contact_form textarea[required=true]").val(''); 
					$("#contact_form #contact_body").slideUp(); //hide form after success
				}
				$("#contact_form #contact_results").hide().html(output).slideDown();
            }, 'json');
        }
    });
    
    //reset previously set border colors and hide all message on .keyup()
    $("#contact_form  input[required=true], #contact_form textarea[required=true]").keyup(function() { 
        $(this).css('background-color',''); 
        $("#result").slideUp();
    });
});
