package com.lightsout.resource;

import com.lightsout.model.Solution;
import com.lightsout.model.SolutionStep;
import com.lightsout.resource.dto.SolutionDTO;
import com.lightsout.resource.dto.SolutionStepDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/solutions")
@Produces(MediaType.APPLICATION_JSON)
public class SolutionsResource {

    @GET
    public List<Solution> getAllSolutions() {
        return Solution.listAll();
    }


    @GET
    @Path("/problem/{id}")
    public SolutionDTO getSolutionForProblem(@PathParam("id") Long problemId) {
        Solution solution = Solution.find("problem.id", problemId).firstResult();

        if (solution == null) {
            throw new NotFoundException("Solution not found for problem " + problemId);
        }

        List<SolutionStepDTO> steps = solution.steps.stream()
                .map(s -> new SolutionStepDTO(s.x, s.y, s.stepOrder))
                .collect(Collectors.toList());

        return new SolutionDTO(solution.id, solution.problem.id, steps);
    }
}
