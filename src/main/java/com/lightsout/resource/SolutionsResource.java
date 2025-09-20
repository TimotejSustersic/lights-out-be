package com.lightsout.resource;

import com.lightsout.model.Solution;
import com.lightsout.resource.dto.SolutionDTO;
import com.lightsout.resource.dto.SolutionStepDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/solutions")
@Produces(MediaType.APPLICATION_JSON)
public class SolutionsResource {

    @GET
    public Response getAllSolutions() {
        List<SolutionDTO> solutions = Solution.<Solution>listAll().stream()
                .map(s -> new SolutionDTO(
                        s.id,
                        s.problem.id,
                        s.steps.stream().map(step -> new SolutionStepDTO(step.x, step.y)).toList()
                ))
                .toList();
        return Response.ok(solutions).build();
    }


    @GET
    @Path("/problem/{id}")
    public SolutionDTO getSolutionForProblem(@PathParam("id") Long problemId) {
        Solution solution = Solution.find("problem.id", problemId).firstResult();

        if (solution == null) {
            throw new NotFoundException("Solution not found for problem " + problemId);
        }

        List<SolutionStepDTO> steps = solution.steps.stream()
                .map(s -> new SolutionStepDTO(s.x, s.y))
                .toList();

        return new SolutionDTO(solution.id, solution.problem.id, steps);
    }
}
