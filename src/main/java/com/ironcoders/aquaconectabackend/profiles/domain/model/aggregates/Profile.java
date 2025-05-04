package com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateProfileCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.UpdateProfileCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.valueobjects.PersonName;
import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class Profile extends AuditableAbstractAggregateRoot<Profile> {
    @Embedded
    private PersonName name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String direction;
    @Column(nullable = false)
    private String documentNumber;
    @Column(nullable = false)
    private String documentType;
    private Long userId;

    @Column(nullable = false)
    private String phone;


    public Profile(PersonName name, String email, String direction, String documentNumber, String documentType, Long userId, String phone) {
        this.name = name;
        this.email = email;
        this.direction = direction;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.phone = phone;
        this.userId = userId;

    }

    public Profile(CreateProfileCommand command, Long userId) {
        this.name = new PersonName(command.firstName(), command.lastName());
        this.email = command.email();
        this.direction = command.direction();
        this.documentNumber = command.documentNumber();
        this.documentType = command.documentType();
        this.phone = command.phone();
        this.userId = userId;
    }

    public Profile() {}

    public void updateName(String firstName, String lastName) {
        this.name = new PersonName(firstName, lastName);
    }

    public String getFullName() { return name.getFullName(); }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return name.getFirstName();
    }
    public  String getLastName() {
        return name.getLastName();
    }


    public Profile(UpdateProfileCommand command){
        this.name = new PersonName(command.firstName(), command.lastName());
        this.email= command.email();
        this.documentNumber = command.documentNumber();
        this.documentType = command.documentType();
        this.phone = command.phone();
    }

    public void setName(PersonName name) {
        this.name = name;
    }

}
