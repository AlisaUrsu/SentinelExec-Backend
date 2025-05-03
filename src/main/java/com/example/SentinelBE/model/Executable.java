package com.example.SentinelBE.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.JSONObject;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "executables")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Executable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "users_executables",
            joinColumns = @JoinColumn(name = "executable_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> reporters;

    private String name;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rawFeatures", columnDefinition = "jsonb")
    private Map<String, Object> rawFeatures;


    @Column(precision = 5, scale = 4)
    private BigDecimal score;


    @OneToMany(mappedBy = "executable")
    private List<Scan> scans;

    @OneToOne(mappedBy = "executable")
    private Discussion discussions;

    @CreationTimestamp
    @Column(nullable = false, name = "first_detection", updatable = false)
    private LocalDateTime firstDetection;

    @Column(nullable = true, name = "first_report")
    private LocalDateTime firstReport;

    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;

}
