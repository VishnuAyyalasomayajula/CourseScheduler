package Assignment01.Assignmentcoen6761;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
       
        if (numCourses <= 0) {
            throw new IllegalArgumentException("numCourses must be positive");
        }
        if (prerequisites == null) {
            throw new IllegalArgumentException("prerequisites cannot be null");
        }

        List<Integer> ans = new ArrayList<>();
        List<Integer>[] graph = new List[numCourses];
        int[] inDegrees = new int[numCourses];

        for (int i = 0; i < numCourses; ++i) {
            graph[i] = new ArrayList<>();
        }

        // **Build the graph and validate prerequisites**
        for (int[] prerequisite : prerequisites) {
            if (prerequisite.length != 2) {
                throw new IllegalArgumentException("Invalid prerequisite format");
            }
            int u = prerequisite[1];
            int v = prerequisite[0];

            // **Check for out-of-bounds indices**
            if (u < 0 || u >= numCourses || v < 0 || v >= numCourses) {
                throw new IllegalArgumentException("Course index out of bounds");
            }

            // **Check for self-loops**
            if (u == v) {
                throw new IllegalArgumentException("Self-loop detected");
            }

            graph[u].add(v);
            ++inDegrees[v];
        }

        // **Perform topological sorting**
        Queue<Integer> q = IntStream.range(0, numCourses)
                .filter(i -> inDegrees[i] == 0)
                .boxed()
                .collect(Collectors.toCollection(ArrayDeque::new));

        while (!q.isEmpty()) {
            int u = q.poll();
            ans.add(u);
            for (int v : graph[u]) {
                if (--inDegrees[v] == 0) {
                    q.offer(v);
                }
            }
        }

        
        if (ans.size() != numCourses) {
            throw new IllegalArgumentException("Cycle detected in prerequisites");
        }

        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
}
