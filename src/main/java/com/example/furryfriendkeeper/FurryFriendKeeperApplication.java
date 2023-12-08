package com.example.furryfriendkeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.furryfriendkeeper.entities")
public class FurryFriendKeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(FurryFriendKeeperApplication.class, args);
    }

}
