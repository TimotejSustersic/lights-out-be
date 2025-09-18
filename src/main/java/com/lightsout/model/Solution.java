package com.lightsout.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "solution")
public class Solution extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    public Problem problem;

    @OneToMany(mappedBy = "solution", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<SolutionStep> steps;
}
