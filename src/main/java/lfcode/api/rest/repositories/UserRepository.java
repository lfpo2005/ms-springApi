package lfcode.api.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lfcode.api.rest.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
