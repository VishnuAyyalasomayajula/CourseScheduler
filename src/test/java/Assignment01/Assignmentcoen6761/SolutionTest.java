package Assignment01.Assignmentcoen6761;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)  // Enables parallel execution
class SolutionTest {
    
    private Solution solver;
    
    @BeforeEach
    void setUp() {
        solver = new Solution();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ecc2.csv", numLinesToSkip = 1)
    @CsvFileSource(resources = "/bcc2.csv", numLinesToSkip = 1)
    void testFindOrder(String testCaseId, String description, String numCoursesStr, String prerequisitesStr, String expectedOutput) {
        try {
            int numCourses = parseNumCourses(numCoursesStr);
            int[][] prerequisites = parsePrerequisites(prerequisitesStr);

            int[] result = solver.findOrder(numCourses, prerequisites);
            String resultStr = Arrays.toString(result);

            assertEquals(expectedOutput, resultStr,
                         "Test Case " + testCaseId + " failed: Expected " + expectedOutput + " but got " + resultStr);
        } catch (IllegalArgumentException e) {
            if (!expectedOutput.equals("IllegalArgumentException")) {
                fail("Test Case " + testCaseId + " failed: Unexpected exception occurred: " + e.getMessage());
            }
        }
    }

    private int parseNumCourses(String str) {
        if (str.equalsIgnoreCase("empty")) {
            throw new IllegalArgumentException("numCourses cannot be empty");
        }
        return Integer.parseInt(str);
    }

    private int[][] parsePrerequisites(String str) {
        if (str.equalsIgnoreCase("null")) {
            return null;
        }
        if (str.equals("[]")) {
            return new int[0][];
        }

        // Remove the outer brackets
        str = str.substring(2, str.length() - 2);

        // Split into individual prerequisite pairs
        String[] pairs = str.split("\\],\\[");
        int[][] prerequisites = new int[pairs.length][2];

        for (int i = 0; i < pairs.length; i++) {
            String[] values = pairs[i].split(",");
            if (values.length != 2) {
                throw new IllegalArgumentException("Invalid prerequisite pair: " + pairs[i]);
            }
            prerequisites[i][0] = Integer.parseInt(values[0].trim());
            prerequisites[i][1] = Integer.parseInt(values[1].trim());
        }

        return prerequisites;
    }
}