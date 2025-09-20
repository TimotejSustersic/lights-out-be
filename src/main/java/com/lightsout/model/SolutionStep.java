package com.lightsout.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "solution_step")
public class SolutionStep extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", nullable = false)
    public Solution solution;

    public int x;
    public int y;

    // this was before I realized order is unimportant
    // @Column(name = "step_order")
    // public int stepOrder; // use snake_case in DB

    public SolutionStep() {
    }
}
