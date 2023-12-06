package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.dtos.ReviewDTO;
import com.example.furryfriendkeeper.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "select avg(Stars) from reviews where PetKeeperId = ?1",nativeQuery = true)
    Double avgStars(Integer id);


    @Query("SELECT r.id AS id, r.comment AS comment, r.stars AS stars FROM Review r WHERE r.petKeeper.id = :petkeeperId")
    List<ReviewDTO> findReviewsByPetkeeperId(@Param("petkeeperId") Integer petkeeperId);

}
