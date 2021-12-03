package lfcode.api.rest.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lfcode.api.rest.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select u from User u where u.username = ?1")
    User findUserByLogin(String username);

}
