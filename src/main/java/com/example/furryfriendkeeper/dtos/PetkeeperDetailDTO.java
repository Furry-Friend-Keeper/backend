package com.example.furryfriendkeeper.dtos;
import com.example.furryfriendkeeper.entities.Address;
import com.example.furryfriendkeeper.entities.Review;
import com.example.furryfriendkeeper.repositories.AddressRepository;
import com.example.furryfriendkeeper.repositories.GalleryRepository;
import com.example.furryfriendkeeper.repositories.ReviewRepository;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetkeeperDetailDTO {

    private Integer id;
    @NotEmpty
    private String name;
    private List<String> gallery;
    private String detail;
    @NotEmpty
    private String contact;
    @NotEmpty
    private Integer phone;
    private String img;
    @NotEmpty
    private String email;
    private double reviewStars;
    private List<ReviewDTO> reviews;
    @NotEmpty
    private AddressDTO address;
    private List<String> categories;

    public void setReviewFromPetkeeperId(Integer id, ReviewRepository repository){
        this.reviews = repository.findReviewsByPetkeeperId(id);
    }
    public void setAddressFromPetkeeperId(Integer id, AddressRepository repository){
        this.address = repository.findAddressesByPetkeeperId(id);
    }

}
