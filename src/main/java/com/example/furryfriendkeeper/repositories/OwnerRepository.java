package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.dtos.OwnerDetailDTO;
import com.example.furryfriendkeeper.entities.Petowner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Petowner,Integer> {
    @Query(value = "select PetOwnerId from petowner where Email = :email",nativeQuery = true)
    Integer getPetownerIdByEmail(String email);

    @Query(value = "select o.PetOwnerId o.FirstName, o.LastName, o.Phone, o.Img, O.Petname, o.Email from petowner o where o.PetOwnerId = ?1",nativeQuery = true)
    OwnerDetailDTO getOwnerDetail(Integer petOwnerId);
}
