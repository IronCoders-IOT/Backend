package com.ironcoders.aquaconectabackend.management.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Arrays;
import java.util.Objects;


@Embeddable
public class RequestStatus {

    public enum Status {
        RECEIVED,
        IN_PROGRESS,
        CLOSED
    }

    @Enumerated(EnumType.STRING)
    private Status value;

    protected RequestStatus() {} // Constructor para JPA

    public RequestStatus(Status value) {
        this.value = value;
    }

    public RequestStatus(String value) {
        this.value = Status.valueOf(value.trim().toUpperCase().replace(" ", "_"));
    }

    public Status getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestStatus that)) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.name();
    }
}