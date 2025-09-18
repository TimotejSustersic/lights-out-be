package com.lightsout.resource.dto;

import java.util.List;

public class SolutionDTO {
    public Long id;
    public Long problemId;
    public List<SolutionStepDTO> steps;

    public SolutionDTO(Long id, Long problemId, List<SolutionStepDTO> steps) {
        this.id = id;
        this.problemId = problemId;
        this.steps = steps;
    }
}
