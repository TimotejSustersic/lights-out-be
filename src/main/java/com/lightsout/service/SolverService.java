package com.lightsout.service;

import com.lightsout.model.Problem;
import com.lightsout.model.Solution;
import com.lightsout.model.SolutionStep;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class SolverService {

    private long getTime(Instant start) {
        return Duration.between(start, Instant.now()).toMillis();
    }

    public SolverResult solve(Problem problem) {
        var start = Instant.now();

        int moves = 0;
        boolean solutionFound = false;
        var steps = new ArrayList<>();

        // alg var
        var visited = new HashSet<String>();
        var queue = new LinkedList<Node>();

        // add initial node
        Node node = new Node(problem.grid, null, -1, -1);
        queue.add(node);
        visited.add(node.hash());

        // try everything once until you get the solution
        while (!queue.isEmpty()) {
            node = queue.poll();
            moves++;

            if (node.isComplete()) {
                solutionFound = true;
                break;
            }

            // get all combinations from the current
            for (Node child : node.expand()) {
                // if we haven't yet seen them, add them to the queue
                if (!visited.contains(child.hash())) {
                    queue.add(child);
                }
            }
        }

        if (!solutionFound)
            return new SolverResult(false, getTime(start), 0, null);

        var solution = getSolution(problem, node);

        return new SolverResult(solutionFound, getTime(start), moves, solution);
    }


    public static class Node {
        int[][] matrix;
        Node parent;
        int x;
        int y;

        Node(int[][] matrix, Node parent, int x, int y) {
            this.matrix = matrix;
            this.parent = parent;
            this.x = x;
            this.y = y;
        }

        boolean isComplete() {
            for (int[] row : matrix) {
                for (int cell : row) {
                    if (cell != 1) return false;
                }
            }
            return true;
        }

        String hash() {
            var sb = new StringBuilder();
            for (int[] row : matrix) {
                for (int cell : row) {
                    sb.append(cell);
                }
            }
            return sb.toString();
        }

        List<Node> expand() {
            List<Node> children = new ArrayList<>();
            int size = matrix.length;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                        var newMatrix = deepCopy(matrix);
                        toggle(newMatrix, i, j);
                        children.add(new Node(newMatrix, this, i, j));
                }
            }

            return children;
        }

        private void toggle(int[][] matrix, int i, int j) {
            int size = matrix.length;

            int[][] directions = {{0, 0}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] offset : directions) {
                int row = i + offset[0];
                int column = j + offset[1];
                if (row >= 0 && row < size && column >= 0 && column < size) {
                    matrix[row][column] = matrix[row][column] == 1 ? 0 : 1;
                }
            }
        }

        private static int[][] deepCopy(int[][] src) {
            var copy = new int[src.length][src[0].length];
            for (int i = 0; i < src.length; i++) {
                copy[i] = Arrays.copyOf(src[i], src[i].length);
            }
            return copy;
        }
    }

    protected Solution getSolution(Problem problem, Node node) {
        Solution solution = new Solution();
        solution.problem = problem;
        solution.persist();

        storeSolutionSteps(solution, node, 0);

        return solution;
    }

    protected int storeSolutionSteps(Solution solution, Node node, int parentOrder) {

        if (node == null)
            return parentOrder;

        int order = storeSolutionSteps(solution, node.parent, parentOrder);

        var step = new SolutionStep();
        step.solution = solution;
        step.x = node.x;
        step.y = node.y;
        step.stepOrder = order;
        step.persist();

        return order + 1;
    }

    public static class SolverResult {
        public boolean success;
        public long timeMs;
        public int moves;
        public Solution solution;

        public SolverResult(boolean success, long timeMs, int moves, Solution solution) {
            this.success = success;
            this.timeMs = timeMs;
            this.moves = moves;
            this.solution = solution;
        }
    }

}

