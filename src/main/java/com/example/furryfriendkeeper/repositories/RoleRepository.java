package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select Role from roles where RoleId = :id",nativeQuery = true)
    String RoleName(Integer id);


}
