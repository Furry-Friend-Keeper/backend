package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.AddressDTO;
import com.example.furryfriendkeeper.dtos.SignUpPetkeeperDTO;
import com.example.furryfriendkeeper.entities.Address;
import com.example.furryfriendkeeper.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Address save(SignUpPetkeeperDTO address){
        Address address1 = modelMapper.map(address, Address.class);
        return addressRepository.saveAndFlush(address1);
        }
}
