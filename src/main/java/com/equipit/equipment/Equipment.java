package com.equipit.equipment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private boolean available = true;
    private String borrowerFirstName;
    private String borrowerLastName;
    private LocalDate borrowedAt;
    private LocalDate lastReturnedAt;

    protected Equipment() { }

    public Equipment(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }
    public String getBorrowerFirstName() { return borrowerFirstName; }
    public String getBorrowerLastName() { return borrowerLastName; }
    public LocalDate getBorrowedAt() { return borrowedAt; }
    public LocalDate getLastReturnedAt() { return lastReturnedAt; }

    public void borrow(String borrowerFirstName, String borrowerLastName, LocalDate borrowedAt) {
        this.borrowerFirstName = borrowerFirstName;
        this.borrowerLastName = borrowerLastName;
        this.borrowedAt = borrowedAt;
        this.available = false;
    }
}