package lfcode.api.rest.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lfcode.api.rest.models.UserModel;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {

    @Query("select u from UserModel u where u.login = ?1")
    UserModel findUserByLogin(String login);

}
