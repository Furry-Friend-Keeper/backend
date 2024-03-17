package com.example.furryfriendkeeper.entities;

import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "appointmentschedule")
public class Appointmentschedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentId", nullable = false)
    private Integer id;

    @Column(name = "OwnerPhone", nullable = false, length = 10)
    private String ownerPhone;

    @Column(name = "PetName", length = 200)
    private String petName;

    @Column(name = "Message", length = 200)
    private String message;

    @Column(name = "StartDate", nullable = false)
    private Instant startDate;

    @Column(name = "EndDate", nullable = false)
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetOwnerId", nullable = false)
    private Petowner petOwner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetKeeperId", nullable = false)
    private Petkeepers petKeeper;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CategoryId", nullable = false)
    private Petcategory category;

    public Petcategory getCategory() {
        return category;
    }

    public void setCategory(Petcategory category) {
        this.category = category;
    }

    public Petkeepers getPetKeeper() {
        return petKeeper;
    }

    public void setPetKeeper(Petkeepers petKeeper) {
        this.petKeeper = petKeeper;
    }

    public Petowner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(Petowner petOwner) {
        this.petOwner = petOwner;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}