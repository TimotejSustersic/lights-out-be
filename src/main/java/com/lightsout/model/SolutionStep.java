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

    @Column(name = "step_order")
    public int stepOrder; // use snake_case in DB

    public SolutionStep() {}

    public SolutionStep(Solution solution, int x, int y, int stepOrder) {
        this.solution = solution;
        this.x = x;
        this.y = y;
        this.stepOrder = stepOrder;
    }
}
