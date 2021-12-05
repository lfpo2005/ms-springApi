package lfcode.api.rest.controlles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lfcode.api.rest.models.UserModel;
import lfcode.api.rest.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/user")
public class IndexController {
	
	@Autowired
	UserRepository userRepository;
	
	
	@GetMapping
	public ResponseEntity<List<UserModel>> getAllUsers() {
		
		List<UserModel> users = (List<UserModel>) userRepository.findAll();
		
		
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserModel> getOneUser(@PathVariable(value = "id") Long id){
		
		 Optional<UserModel> userOptional = userRepository.findById(id);


	            return new  ResponseEntity<UserModel>(userOptional.get(), HttpStatus.OK);

		}
	
	@PostMapping(value = "/signup")
	public ResponseEntity<UserModel> registerUser(@RequestBody UserModel user){
		

	        user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
	        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
	        
	        for (int pos = 0; pos < user.getPhone().size(); pos ++ ) {
	        	user.getPhone().get(pos).setUser(user);
	        }
	        userRepository.save(user);

	        return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	
	
	@PutMapping("/updateUser/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable(value = "id") Long id){
		
        Optional<UserModel> userOptional = userRepository.findById(id);

           var user = userOptional.get();
           
           for (int pos = 0; pos < user.getPhone().size(); pos ++ ) {
	        	user.getPhone().get(pos).setUser(user);
	        }
           
           user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

	
	  @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") Long id){
		  
	        Optional<UserModel> userOptional = userRepository.findById(id);

	            userRepository.delete(userOptional.get());
	            return ResponseEntity.status(HttpStatus.OK).body("User deleted success");
	        }

	}
	


