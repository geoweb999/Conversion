import java.util.*;
import java.util.HashMap;

public class Conversion {

    public static void addRule(Map<String, Map<String, Double>> rules, String source, String destination, double conversion) {
        if (!rules.containsKey(source)) {
            Map<String, Double> neighbors = new HashMap<>();
            rules.put(source, neighbors);
        }
        rules.get(source).put(destination, conversion);

        // add inverse
        if (!rules.containsKey(destination)) {
            Map<String, Double> neighbors = new HashMap<>();
            rules.put(destination, neighbors);
        }
        rules.get(destination).put(source, 1.0 / conversion);
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
        System.out.print("Path to conversion: " + path + " ");
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
        addRule(rules, "feet", "inch", 12);
        addRule(rules, "hour", "minute", 60);
        addRule(rules, "yard", "feet", 3);
        addRule(rules, "km", "mile", 0.6213712);
        addRule(rules, "km", "meter", 1000);
        addRule(rules, "yard", "meter", 0.9144);
        addRule(rules, "furlong", "meter", 201.0);
        addRule(rules, "Angstrom", "nanometer", 0.1);
        addRule(rules, "mm", "micrometer", 1000.0);
        addRule(rules, "micrometer", "nanometer", 1000.0);
        addRule(rules, "nm", "mile", 1.150779);
        addRule(rules, "rod", "yard", 5.5);
        addRule(rules, "year", "day", 365);
        addRule(rules, "day", "hour", 24);
        addRule(rules, "minute", "second", 60);


        String source = "mile";
        String dest = "rod";
        double factor = getRule(rules, source, dest );
        System.out.println("From " + source + " to " + dest + " = " + String.format("%.2f",factor));

        source = "mile";
        dest = "feet";
        factor = getRule(rules, source, dest );
        System.out.println("From " + source + " to " + dest + " = " + String.format("%.2f",factor));

        source = "meter";
        dest = "inch";
        factor = getRule(rules, source, dest );
        System.out.println("From " + source + " to " + dest + " = " + String.format("%.2f",factor));

        source = "mile";
        dest = "furlong";
        factor = getRule(rules, source, dest );
        System.out.println("From " + source + " to " + dest + " = " + String.format("%.2f",factor));

        source = "year";
        dest = "second";
        factor = getRule(rules, source, dest );
        System.out.println("From " + source + " to " + dest + " = " + String.format("%.2f",factor));
    }
}