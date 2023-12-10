package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.dtos.AddressDTO;
import com.example.furryfriendkeeper.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query(value = "SELECT a.AddressId, a.Address, a.District, a.Province, a.PostalCode, a.Map FROM address a JOIN petkeepers p ON a.AddressId = p.AddressId where p.PetKeeperId = :petkeeperId",nativeQuery = true)
    AddressDTO findAddressesByPetkeeperId(@Param("petkeeperId") Integer petkeeperId);

    @Query(value = "select * from address a JOIN petkeepers p ON a.AddressId = p.AddressId where p.PetKeeperId = :petkeeperId",nativeQuery = true)
    Address findAddressDetailByPetkeeperId(Integer petkeeperId);



}
