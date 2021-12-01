package lfcode.api.rest.controlles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/user")
public class IndexController {
	
	@GetMapping(value = "/")
	public ResponseEntity init() {
		 return  new ResponseEntity("testenado", HttpStatus.OK);
	}
	

}
