package com.lightsout.model;

import com.lightsout.model.utils.GridConverter;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Problem extends PanacheEntity {

    @Convert(converter = GridConverter.class)
    @Column(columnDefinition = "CLOB")
    public int[][] grid;

    public Problem() {}
}
