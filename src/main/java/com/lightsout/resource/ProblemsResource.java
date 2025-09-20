package com.lightsout.resource;

import com.lightsout.service.LinearAlgebraSolver;
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
    LinearAlgebraSolver solverService;

    @POST
    @Transactional
    public Response createProblem(Problem problem) {
        problem.id = null;

        var result = solverService.solve(problem);

        return Response.ok()
                .entity(Map.of(
                        "success", result.success,
                        "timeMs", result.timeMs,
                        "moves", result.moves
                ))
                .build();
    }
}
