$(function() {
	
    $("#regx").click(function() {
    //event.preventDefault();
    $('#message').css({ display: "none" });
    $('#messageError').css({ display: "none" });
      // validate and process form here
      //alert($("input#email").val()+""+ $("input#name").val()+""+ $("input#pass").val()+""+ $("input#pass2").val());
    
    var x=/^[A-Za-z0-9]*$/;
        
      var email = $("input#email").val();
      if (email == "") {
    $("label#email_error").show();
     $("input#email").focus();
    return false;
  }
      



  	  var name = $("input#name").val();
  		if (name == "" ) {
        $("label#name_error").show();
        $("input#name").focus();
        return false;}
        
  		if (name.length<=3){
  	        $("label#name_error").show();
  	        $("input#name").focus();
  	      alert("Username must have atleast 4 characters!");
  	      return false;
  		}
	  		if (!/^[a-zA-Z0-9]*$/.test(name)){
	  	        $("label#name_error").show();
	  	        $("input#name").focus();
	  	      alert("Username characters must be alphanumeric!");
	  	      return false;
	  		}
  		
          	  var password1 = $("input#password1").val();
  		if (password1 == "") {
        $("label#password1_error").show();
        $("input#password1").focus();
        return false;}
  		
  		if (password1.length<=3){
  	        $("label#password1_error").show();
  	        $("input#password1").focus();
  	      alert("Password must have atleast 4 characters!");
  	      return false;
  		}
  		
  		  		if (!/^[\x21-\x7E]*$/.test(password1)){
  	        $("label#password1_error").show();
  	        $("input#password1").focus();
  	      alert("Password characters must be alphanumeric or symbols (no space)!");
  	      return false;
  		}
  		
          	  var password2 = $("input#password2").val();
  		if (password2 == "") {
        $("label#password2_error").show();
        $("input#password2").focus();
        return false;}
  		
  		if (password2!=password1){
  	        $("label#password1_error").show();
  	        $("input#password1").focus();
  	      alert("Password confirmation does not match!");
  	      return false;
  		}
        
      var dataString = 'email=' + email+'&name='+ name +'&password1=' + password1+'&password2=' + password2;
      //alert (dataString);return false;
      
      
      $.ajax({
    type: "POST",
    url: "api/register/",
    data: dataString,

    
  }).done(function(msg) {
      
      $('#message').css({ display: "block" });
      $(".input").val("");
      $("input#login").val(email);
      $("input#password").val(password1);
      
      //alert(msg);
    }).fail(function(errMsg,code) {
    //alert(errMsg.responseText);
     $('#errorMsgAccount').text(errMsg.responseText);
     $('#messageError').css({ display: "block" });
    });
  return false;
    });
    
       $(".xloginbutton").click(function() {
      var login = $("input#login").val();
  		if (login == "") {
        $("label#login_error").show();
        $("input#login").focus();
        return false;}
        
          	  var password = $("input#password").val();
  		if (password == "") {
        $("label#password_error").show();
        $("input#password").focus();
        return false;}
        
         var dataString = 'username=' + login+'&password=' + password;
         
         
         
         $.ajax({
    type: "POST",
    url: "login",
    data: dataString,

    
  }).done(function(response){
  						
                        
                    })
                    .fail(function(errMsg,code) {
    //alert("hi");

    });
  return false;
         
    });
    
  });
  
