package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.entities.Petcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Petcategory, Integer> {
    @Query(value = "select CategoriesId from petcategories p where p.PetKeeperId = ?1",nativeQuery = true)
    List<Integer> FindKeeperCategories(Integer Id);

    @Query(value = "select c.Name from petcategories p join pets c on p.CategoriesId = c.CategoriesId where p.PetKeeperId = :id",nativeQuery = true)
    List<String> FindCategoriesByPetkeeperId(Integer id);
}
