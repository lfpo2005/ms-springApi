package lfcode.api.rest.controlles;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lfcode.api.rest.dtos.UserDto;
import lfcode.api.rest.enums.UserStatus;
import lfcode.api.rest.models.UserModel;
import lfcode.api.rest.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	UserRepository userRepository;
	
	
	@PostMapping(value = "/signup")
	public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
	@JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
		
		
		if (userRepository.existsByLogin(userDto.getLogin())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
		}
		
		 if(userRepository.existsByEmail(userDto.getEmail())){
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
	    }

			var userModel = new UserModel();
				        
	        
			BeanUtils.copyProperties(userDto, userModel);
			userModel.setUserStatus(UserStatus.ACTIVE);

			userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
			userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			String passwordCripto = new BCryptPasswordEncoder().encode(userModel.getPassword());
	        userModel.setPassword(passwordCripto);
	        

			userRepository.save(userModel);

			return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	       
	}
	
	
	

}
