package com.example.BeautServices.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)  // Add constraint
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image")
    private byte[] image;

    @Column(length = 1000)  // Add length limit for description
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BeautService> services = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    public void addService(BeautService service) {
        services.add(service);
        service.setCategory(this);
    }

    // Remove all manual getters and setters since we're using Lombok
}