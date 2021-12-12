package lfcode.api.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lfcode.api.rest.models.UserModel;
import lfcode.api.rest.repositories.UserRepository;

@Service
public class ImplUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    	UserModel userModel = userRepository.findUserByLogin(username);

        if (userModel == null){
            throw  new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
        		userModel.getLogin(),
        		userModel.getPassword(),
        		userModel.getAuthorities());
    }
}
