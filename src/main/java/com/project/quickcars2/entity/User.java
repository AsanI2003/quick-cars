package com.project.quickcars2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String email;

    @Column(nullable = false)
    private String name;






}

