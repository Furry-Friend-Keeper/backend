package com.example.furryfriendkeeper.dtos;
import com.example.furryfriendkeeper.entities.Address;
import com.example.furryfriendkeeper.entities.Review;
import com.example.furryfriendkeeper.repositories.AddressRepository;
import com.example.furryfriendkeeper.repositories.GalleryRepository;
import com.example.furryfriendkeeper.repositories.ReviewRepository;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetkeeperDetailDTO {

    private Integer id;
    private String name;
    private List<String> gallery;
    private String detail;
    private String contact;
    private Integer phone;
    private String email;
    private double reviewStars;
    private List<ReviewDTO> reviews;
    private AddressDTO address;
    private List<String> categories;

    public void setReviewFromPetkeeperId(Integer id, ReviewRepository repository){
        this.reviews = repository.findReviewsByPetkeeperId(id);
    }
    public void setAddressFromPetkeeperId(Integer id, AddressRepository repository){
        this.address = repository.findAddressesByPetkeeperId(id);
    }

}
