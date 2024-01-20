package com.project.ecommerce.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(max = 20, message = "First name must be less than 20 characters long")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First Name must not contain numbers or special characters")
    private String firstname;

    @Size(max = 20, message = "Last Name must be less than 20 characters long")
	@Pattern(regexp = "^[a-zA-Z]*$", message = "Last Name must not contain numbers or special characters")
    private String lastname;

    @Column(name = "phone_number")
    @Size(min = 10, max = 10, message = "Phone Number must be exactly 10 digits long")
	@Pattern(regexp = "^\\d{10}$", message = "Phone Number must contain only Numbers")
    private String phoneNumber;

    @Email
	@Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
	private Cart cart;

    public User(String firstname, String lastname, String phoneNumber, String email, String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
