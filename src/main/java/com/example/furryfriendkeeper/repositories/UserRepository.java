package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from users u where u.name like ?1",nativeQuery = true)
    List<User> uniqueUserName(String name);

    @Query(value = "select * from users u where u.email like ?1",nativeQuery = true)
    List<User> uniqueUserEmail(String name);

    @Query(value = "select * from users u where u.email = ?1",nativeQuery = true)
    User findEmail(String email);
    Optional<User> findByEmail (String email);

    @Query(value = "select Role from roles r join users u on r.RoleId = u.Role where u.email = ?1",nativeQuery = true)
    String findRole(String email);

    @Query(value = "select user_id from users u where u.email = ?1",nativeQuery = true)
    String findId(String email);
}
