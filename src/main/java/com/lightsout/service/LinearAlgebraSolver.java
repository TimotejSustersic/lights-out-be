package com.lightsout.service;

import com.lightsout.model.Problem;
import com.lightsout.model.Solution;
import com.lightsout.model.SolutionStep;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class LinearAlgebraSolver {

    public SolverResult solve(Problem problem) {

        int n = problem.size;
        int N = n * n;

        if (problem.grid.length != N) {
            throw new IllegalArgumentException("Problem doesn't match grid size");
        }

        // Main algorithm
        var start = Instant.now();
        int[] solution = getSolution(getAMatrix(n, N), problem.grid, N);
        long timeSpent = getTime(start);

        if (solution == null) {
            System.out.println("Problem not solved: time=" + timeSpent + "ms");
            return new SolverResult(false, timeSpent, 0);
        }

        // save problem, solution is found
        problem.timestamp = start;
        problem.persist();

        Solution solutionRecord = new Solution();
        solutionRecord.problem = problem;
        solutionRecord.persist();

        int moves = 0;
        for (int i = 0; i < N; i++) {
            if (solution[i] == 1) {
                moves++;
                var solutionStep = new SolutionStep();
                solutionStep.x = i / n; // int rounds down
                solutionStep.y = i % n;
                solutionStep.solution = solutionRecord;
                solutionStep.persist();
            }
        }

        problem.difficulty = (double) moves / N;
        problem.moves = moves;

        System.out.println("Problem solved: moves=" + moves + ", time=" + timeSpent + "ms");

        return new SolverResult(true, timeSpent, moves);
    }

    private long getTime(Instant start) {
        return Duration.between(start, Instant.now()).toMillis();
    }

    // we have the A matrix of all moves. we have or problem b, and return the solution x
    private int[] getSolution(int[][] A, int[] b, int N) {

        // it's written to turn all the lights off so I flip the problem
        int[] x = new int[N];
        for (int i = 0; i < N; i++) {
            x[i] = b[i] ^ 1;
        }

        for (int i = 0; i < N; i++) {

            // if the diagonal is not one find one below that is and swap
            if (A[i][i] != 1) {
                for (int j = i + 1; j < N; j++) {
                    if (A[j][i] == 1) {
                        // swap the A matrix
                        var tmp = A[i];
                        A[i] = A[j];
                        A[j] = tmp;

                        // swap the b(solution/problem) matrix
                        var tmp2 = x[i];
                        x[i] = x[j];
                        x[j] = tmp2;

                        break;
                    }
                }
            }

            // if any below are not 0 addition with current (mod2(XOR) will remove the one)
            for (int j = i + 1; j < N; j++) {
                if (A[j][i] == 1) {
                    // Addition(XOR) the whole row
                    for (int k = i; k < N; k++) {
                        A[j][k] ^= A[i][k];
                    }
                    // addition(XOR) the problem/solution
                    x[j] ^= x[i];
                }
            }
        }

        // Check for no solution
        for (int i = 0; i < N; i++) {
            if (A[i][i] == 0 && x[i] == 1) {
                return null;
            }
        }

        // Back substitution to remove dependencies and get the full solution
        for (int i = N - 1; i >= 0; i--) {
            if (A[i][i] == 1) {
                for (int j = 0; j < i; j++) {
                    if (A[j][i] == 1) {
                        x[j] ^= x[i]; // Only update x
                    }
                }
            }
        }

        return x;
    }

    private int[][] getAMatrix(int n, int N) {

        var movesMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {

            // pressed button
            movesMatrix[i][i] = 1;
            // + n is 1
            if (i + n < N)
                movesMatrix[i][i + n] = 1;
            // -n is 1
            if (i - n >= 0)
                movesMatrix[i][i - n] = 1;
            // +1 is 1
            if (i % n < n - 1)
                movesMatrix[i][i + 1] = 1;
            // -1 is 1
            if (i % n > 0)
                movesMatrix[i][i - 1] = 1;
        }

        return movesMatrix;
    }

    public static class SolverResult {
        public boolean success;
        public long timeMs;
        public int moves;

        public SolverResult(boolean success, long timeMs, int moves) {
            this.success = success;
            this.timeMs = timeMs;
            this.moves = moves;
        }
    }

}

