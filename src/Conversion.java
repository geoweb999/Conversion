import java.util.*;
import java.util.HashMap;

public class Conversion {

    public static void addRule(Map<String, Map<String, Double>> rules, String source, String destination, double conversion) {
        if (!rules.containsKey(source)) {
            Map<String, Double> neighbors = new HashMap<>();
            rules.put(source, neighbors);
        }
        rules.get(source).put(destination, conversion);
        System.out.println(rules);
    }

    public static double getRule(Map<String, Map<String, Double>> rules, String source, String destination) {
        // source and destination must be in the rule set
        if (!rules.containsKey(source) || !rules.containsKey(destination)) {
            return 0;
        }
        // tracks vertices which have already been visited
        Set<String> visited = new HashSet<>();
        // tracks the relationship between current vertex and how we got here
        Map<String, String> predecessor = new HashMap<>();
        // queue of vertices to visit
        Queue<String> vertexQueue = new LinkedList<>();
        vertexQueue.add(source);
        boolean found = false;

        while (!found && !vertexQueue.isEmpty()) {
            String vertex = vertexQueue.remove();
            visited.add(vertex);
            Map<String, Double> neighbors = rules.get(vertex);
            for (String dest : neighbors.keySet()) {
                if (dest.equals(destination)) {
                    found = true;
                }
                if (!visited.contains(dest)) {
                    visited.add(dest);
                    vertexQueue.add(dest);
                    predecessor.put(dest, vertex);
                }
            }
        }
        if (!found) {
            return 0;
        }

        // take the predecessors to create the path from source to destination
        // the stack will effectively reverse predecessors
        boolean done = false;
        String previous = destination;
        Stack<String> path = new Stack<>();
        path.add(destination);
        while (!done) {
            path.add(predecessor.get(previous));
            previous = predecessor.get(previous);
            done = (previous.equals(source));
        }

        // calculate
        double factor = 1;
        String start = path.pop();
        while (!path.isEmpty()) {
            String end = path.pop();
            Map<String, Double> neighbors = rules.get(start);
            factor *= neighbors.get(end);
            start = end;
        }
        return factor;
    }
    public static void main(String[] args) {

        Map<String, Map<String, Double>> rules = new HashMap<>();
        addRule(rules, "feet", "inches", 12);
        addRule(rules, "hours", "minutes", 60);
        addRule(rules, "inches", "feet",  1.0/12.0);
        addRule(rules, "minutes", "hours", 1.0/60);
        addRule(rules, "yards", "feet", 3);
        addRule(rules, "feet", "yards", 1.0/3);

        String source = "yards";
        String dest = "inches";
        double factor = getRule(rules, source, dest );
        System.out.print("From " + source + " to " + dest + " = " +factor);
    }
}