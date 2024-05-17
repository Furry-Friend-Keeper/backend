package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    @Query(value = "select * from users u where u.Email like ?1", nativeQuery = true)
    List<User> uniqueUserEmail(String email);


    @Query(value = "select u from User u join fetch u.role where u.email = ?1")
    User findEmail(String email);

    @Query(value = "select r.Role from roles r join users u on r.RoleId = u.RoleId where u.email = ?1", nativeQuery = true)
    String findRole(String email);


}
