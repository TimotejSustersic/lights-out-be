package com.lightsout.resource;

import com.lightsout.service.SolverService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.*;

import com.lightsout.model.Problem;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;


@Path("/problems")
@Produces(MediaType.APPLICATION_JSON)
public class ProblemsResource {

    @GET
    public List<Problem> getAllProblems() {
        // Sort by size ascending
        return Problem.list("ORDER BY size ASC");
    }

    @GET
    @Path("{id}")
    public Problem getProblem(@PathParam("id") long id) {
        Problem p = Problem.findById(id);
        if (p == null) throw new NotFoundException();
        return p;
    }

    @Inject
    SolverService solverService;

    @POST
    @Transactional
    public Response createProblem(Problem problem) {
        problem.id = null;

        var result = solverService.solve(problem);

        if (!result.success) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "No solution found"))
                    .build();
        }

        return Response.ok()
                .entity(Map.of(
                        "timeMs", result.timeMs,
                        "moves", result.moves,
                        "solutionId", result.solution.id
                ))
                .build();
    }
}
