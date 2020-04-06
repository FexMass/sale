package org.collibra.challenge.algorithm;

import java.util.*;

/**
 * Implementation of Dijkstra’s Shortest Path Algorithm in Java. Dijkstra’s Algorithms describes
 * how to find the shortest path from one node to another node in a directed weighted graph.
 * A graph is made out of nodes and directed edges which define a connection from one node to another node.
 * The mathematical description for graphs is G= {V,E}, meaning that a graph is defined by a set of
 * vertexes (nodes)(V) and a collection of edges. This implementation does not use any performance optimization (e.g. by using a
 * PriorityQueue for the UnSettledNodes and does not cache the result of the target evaluation of the edges)
 * to make the algorithm as simple as possible.
 */
final class AlgorithmImplementation {

    private List<Edge> edges;
    private Set<String> settledNodes;
    private Set<String> unSettledNodes;
    private Map<String, String> predecessors;
    private Map<String, Integer> distance;
    private Map<String, Integer> distanceCloserThan;

    AlgorithmImplementation(DirectedGraph graph) {
        //create a copy of the array so that we can operate on this array
        this.edges = new ArrayList<>(graph.getEdges());
    }

    /**
     * @param sourceNode sourceNode node name
     */
    void execute(String sourceNode) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        distanceCloserThan = new HashMap<>();
        predecessors = new HashMap<>();
        //putting first sourceNode node name and distance 0 as starting point
        distance.put(sourceNode, 0);
        distanceCloserThan.put(sourceNode, 0);
        //adding each node created to state unsettled
        unSettledNodes.add(sourceNode);

        //loop until unSettledNodes are less than 1
        while (unSettledNodes.size() > 0) {
            //
            String node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            //

            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(String sourceNode) {
        //fill the list with current neighbors from source node
        List<String> adjacentNodes = getNeighbors(sourceNode);
        for (String destinationNode : adjacentNodes) {
            if (getShortestDistance(destinationNode) > getShortestDistance(sourceNode)
                    + getDistance(sourceNode, destinationNode)) {
                //distance calculation for shortest path
                distance.put(destinationNode, getShortestDistance(sourceNode)
                        + getDistance(sourceNode, destinationNode));
                //distance calculation for closer than
                distanceCloserThan.put(destinationNode, getDistance(sourceNode, destinationNode));
                predecessors.put(sourceNode, destinationNode);
                unSettledNodes.add(destinationNode);
            }
        }
    }

    private int getDistance(String sourceNode, String destinationNode) {
        //loop through edges and find only two nodes we are looking for and return edge
        // value or if it doesnt exist return Integer.MAX_VALUE
        for (Edge edge : edges) {
            if (edge.getSourceNode().equals(sourceNode)
                    && edge.getDestinationNode().equals(destinationNode)) {
                return edge.getWeight();
            }
        }
        return Integer.MAX_VALUE;
    }

    private List<String> getNeighbors(String sourceNode) {
        //get next neighbor from source node
        List<String> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            //
            if (edge.getSourceNode().equals(sourceNode)
                    && !isSettled(edge.getDestinationNode())) {
                //adding all neighbors from source node
                neighbors.add(edge.getDestinationNode());
            }
        }
        return neighbors;
    }

    private String getMinimum(Set<String> nodes) {
        String minimum = null;
        for (String node : nodes) {
            //first time minimum is null, return first node
            if (minimum == null) {
                minimum = node;
            } else {
                //
                if (getShortestDistance(node) < getShortestDistance(minimum)) {
                    minimum = node;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(String nodeName) {
        return settledNodes.contains(nodeName);
    }

    int getShortestDistance(String destinationNode) {
        //get current distance to destination node
        Integer d = distance.get(destinationNode);
        if (d == null) {
            //return Integer.MAX_VALUE if connections doesnt exist
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    Set<String> getCloserThan(int weight, String sourceNode) {
        Set<String> path = new TreeSet<>();
        String temp = sourceNode;

        if (predecessors.get(temp) == null) {
            return path;
        }
        int tempWeight = 0;
        while (predecessors.get(temp) != null) {
            temp = predecessors.get(temp);
            tempWeight = tempWeight + distanceCloserThan.get(temp);
            if (tempWeight > weight) {
                break;
            }
            path.add(temp);
        }
        return path;
    }
}