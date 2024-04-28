package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Role;
import com.example.furryfriendkeeper.entities.Schedulestatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Schedulestatus, Integer> {

}
