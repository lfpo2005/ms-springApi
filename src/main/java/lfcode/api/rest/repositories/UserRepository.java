package lfcode.api.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lfcode.api.rest.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<Object> {

    @Query("select u from UserModel u where u.login = ?1")
    UserModel findUserByLogin(String login);
    
    boolean existsByLogin(String login);
    
    boolean existsByEmail(String email);
    
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update tb_users set token =?1 where login = ?2")
    void updateTokenUser(String token, String login);

}
