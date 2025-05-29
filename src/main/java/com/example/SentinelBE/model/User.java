package com.example.SentinelBE.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be null.")
    @NotEmpty(message = "Username cannot be empty.")
    @NotBlank(message = "Username cannot be blank.")
    @Column(unique = true)
    private String username;

    @NotNull(message = "Email cannot be null.")
    @NotEmpty(message = "Email cannot be empty.")
    @NotBlank(message = "Email cannot be blank.")
    @Column(unique = true)
    private String email;

    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isEnabled;

    @Lob
    private byte[] profilePicture;

    @ManyToMany(mappedBy = "reporters")
    private List<Executable> reportedExecutables = new ArrayList<>();

    //@OneToMany(mappedBy = "author")
    //private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Scan> scans;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //@Column(nullable = true, name = "last_login")
    //private LocalDateTime lastLogin;


}