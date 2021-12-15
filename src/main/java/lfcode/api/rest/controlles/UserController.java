package lfcode.api.rest.controlles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lfcode.api.rest.dtos.UserDto;
import lfcode.api.rest.models.UserModel;
import lfcode.api.rest.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	// @Cacheable("cacheUserAll")
	@CacheEvict(value = "cacheUserAll", allEntries = true)
	@CachePut("cacheUserAll")
	@GetMapping
	public ResponseEntity<List<UserModel>> getAllUsers() throws InterruptedException {

		List<UserModel> users = (List<UserModel>) userRepository.findAll();

		// Thread.sleep(6000); /** Simula uma sobre carga de 6 segundos para fazer teste
		// cache */

		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") Long id) {

		Optional<UserModel> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		} else {

			return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id) {

		Optional<UserModel> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		} else {
			userRepository.delete(userOptional.get());
			return ResponseEntity.status(HttpStatus.OK).body("User deleted success!");
		}

	}

	@PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id,
    											@RequestBody @Validated(UserDto.UserView.UserPut.class)
    											@JsonView(UserDto.UserView.UserPut.class)UserDto userDto){
		
        Optional<UserModel> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		} else {

           var userModel = userOptional.get();
           
           for (int pos = 0; pos < userModel.getPhone().size(); pos ++ ) {
        	   userModel.getPhone().get(pos).setUser(userModel);
	        }
           
           userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
	       userModel.setFullName(userDto.getFullName());
           
           /*
			 * String passwordCripto = new
			 * BCryptPasswordEncoder().encode(userModel.getPassword());
			 * userModel.setPassword(passwordCripto);
			 */
            userRepository.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
	}
	}
