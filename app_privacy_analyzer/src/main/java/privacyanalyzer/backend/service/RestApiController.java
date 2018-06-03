package privacyanalyzer.backend.service;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import privacyanalyzer.backend.data.Role;
import privacyanalyzer.backend.data.entity.User;
 
 
@RestController
@RequestMapping("/api")
public class RestApiController {
 
    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);
 
    @Autowired
    UserService userService; //Service which will do all data retrieval/manipulation work
 
    
    // -------------------Create a User-------------------------------------------
 
    @RequestMapping(value = "/register/", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> createUser(@RequestParam("email") String email,
    		@RequestParam("name") String name,
    		@RequestParam("password1") String password1,
    		@RequestParam("password2") String password2,
    		UriComponentsBuilder ucBuilder) {
    	

    	boolean valid = EmailValidator.getInstance().isValid(email);
    	if (!valid) {
    		return new ResponseEntity(email + " is not a valid email. Please enter a valid email.",HttpStatus.CONFLICT);
    	}
    	
    	if (name.length()<=3) {
    		return new ResponseEntity("Username must have atleast 4 characters. Please enter a valid username.",HttpStatus.CONFLICT);
    	}

    	if (!StringUtils.isAlphanumeric(name)) {
    		return new ResponseEntity("Username characters must be alphanumeric. Please enter a valid username.",HttpStatus.CONFLICT);
    	}
    	
    	if (password1.length()<=3) {
    		return new ResponseEntity("Password must have atleast 4 characters. Please enter a valid password.",HttpStatus.CONFLICT);
    	}
    	if (!password1.matches("^[\\x21-\\x7E]*$")) {
    		return new ResponseEntity("Password characters must be alphanumeric or symbols (no space). Please enter a valid password.",HttpStatus.CONFLICT);
    	}
    	
    	if (!(password1.equals(password2))) {
    		return new ResponseEntity("Password confirmation does not match",HttpStatus.CONFLICT);
    	}
    	
    	

    	
    	
    	User user= new User(email,name,password1, Role.USER);
        logger.info("Creating User : {}", user);
 
        if (userService.isUserExist(user)) {
            logger.error("Unable to create. A User with name {} already exist. Try a different name.", user.getName());
            return new ResponseEntity("Unable to create. A User with name " + 
            user.getName() + " already exist. Try a different name.",HttpStatus.CONFLICT);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
 
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
 
    private class CustomErrorType {
    	 
        private String errorMessage;
     
        public CustomErrorType(String errorMessage){
            this.errorMessage = errorMessage;
        }
     
        public String getErrorMessage() {
            return errorMessage;
        }
     
    }
 
}