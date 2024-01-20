package com.project.ecommerce.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private ETypeBooking typeBooking;
    
    @NotNull
    @Column(nullable = false, name = "from_date")
    private Date fromDate;

    @Column(nullable = true, name = "to_date")
    private Date toDate;

    // Custom validation method
    @AssertTrue(message = "toDate must be greater than fromDate")
    private boolean isToDateGreaterThanFromDate() {
        if (toDate == null) {
            return true;
        }
        return toDate.after(fromDate);
    }
}
