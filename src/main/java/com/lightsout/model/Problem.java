package com.lightsout.model;

import com.lightsout.model.utils.GridConverter;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Problem extends PanacheEntity {

    @Convert(converter = GridConverter.class)
    @Column(columnDefinition = "CLOB")
    public int[][] grid;

    public int size;
    public Instant timestamp;

    // difficulty (you can store as double or int)
    public double difficulty;

    public Problem() {}
}
