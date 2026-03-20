// FILE PATH: src/main/java/com/scholife1/model/Role.java

package com.scholife1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    // Getters
    public Long getId()         { return id; }
    public String getRoleName() { return roleName; }

    // Setters
    public void setId(Long id)              { this.id = id; }
    public void setRoleName(String roleName){ this.roleName = roleName; }
}