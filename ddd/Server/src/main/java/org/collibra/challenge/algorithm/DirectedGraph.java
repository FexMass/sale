package org.collibra.challenge.algorithm;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * This class represent object containing logic for storing, building graph and sharing among
 * multiple sessions, so it has to be synchronized. Error checking and unusual inputs are handled
 * by this class before calling proper algorithm for execution
 */
public class DirectedGraph {

    private volatile HashMap<String, String> nodes = new HashMap<>();
    private CopyOnWriteArrayList<Edge> edges = new CopyOnWriteArrayList<>();
    private static DirectedGraph DIRECTED_GRAPH_INSTANCE;
    private static final Logger LOGGER = Logger.getLogger(DirectedGraph.class.getName());

    private DirectedGraph() { }

    /**
     * Adding new node to map of nodes if not null
     *
     * @param nodeName node name for adding
     * @return true if successful, false if fail
     */
    public synchronized boolean addNode(String nodeName) {
        LOGGER.info("Add node method triggered");
        if (null == nodes.get(nodeName)) {
            nodes.put(nodeName, nodeName);
            LOGGER.info("Node added: " + nodeName);
            return true;
        }
        return false;
    }

    /**
     * Adding a new edge between source node and destination node with given weight
     *
     * @param sourceNode      source node name
     * @param destinationNode destination node name
     * @param weight          Integer value (weight) of link between two nodes
     * @return true if successful, false if fail
     */
    public synchronized boolean addEdge(String sourceNode, String destinationNode, int weight) {
        LOGGER.info("Add edge method triggered");
        if (null == nodes.get(sourceNode) || null == nodes.get(destinationNode)) {
            return false;
        }
        edges.add(new Edge(sourceNode, destinationNode, weight));
        LOGGER.info("Edge added between nodes: " + sourceNode + "-" + destinationNode + ", with weight: " + weight);
        return true;
    }

    /**
     * Removing node from map of nodes if node exist in map of nodes and deleting all edges attached to that node
     *
     * @param nodeName node name for removal
     * @return true if successful, false if fail
     */
    public synchronized boolean removeNode(String nodeName) {
        LOGGER.info("Remove node method triggered");
        if (null == nodes.get(nodeName)) {
            return false;
        }
        nodes.remove(nodeName);
        LOGGER.info("Node removed: " + nodeName);
        getEdges().forEach((edge) -> {
            if (edge.getSourceNode().contains(nodeName) && edge.getDestinationNode().contains(nodeName)) {
                edges.remove(edge);
                LOGGER.info("Edges removed: " + edge);
            }
        });
        return true;
    }

    /**
     * Removing all edges from source node and destination node
     *
     * @param sourceNode      source node name
     * @param destinationNode destination node name
     * @return true if successful, false if fail
     */
    public synchronized boolean removeEdge(String sourceNode, String destinationNode) {
        LOGGER.info("Remove edge method triggered");
        if (null == nodes.get(sourceNode) || null == nodes.get(destinationNode)) {
            return false;
        }
        getEdges().forEach((edge) -> {
            if (edge.getSourceNode().contains(sourceNode) && edge.getDestinationNode().contains(destinationNode)) {
                edges.remove(edge);
                LOGGER.info("Edge removed between nodes: " + sourceNode + "-" + destinationNode + ", with weight: " + edge.getWeight());
            }
        });
        return true;
    }

    /**
     * Finding shortest path (Integer) between source node and destination node
     *
     * @param sourceNode      source node name
     * @param destinationNode destination node name
     * @return Integer {0} if any of provided nodes doesnt exist or Integer {value} if path has been found
     */
    public synchronized Integer getShortestPath(String sourceNode, String destinationNode) {
        LOGGER.info("Calculating Shortest path");
        AlgorithmImplementation dijkstra = new AlgorithmImplementation(DIRECTED_GRAPH_INSTANCE);
        if (null == getNodes().get(sourceNode) || null == getNodes().get(destinationNode)) {
            return 0;
        }
        dijkstra.execute(getNodes().get(sourceNode));
        return dijkstra.getShortestDistance(getNodes().get(destinationNode));
    }

    /**
     * Finding closer than (Integer) from source node to all nodes with smaller (sum) weight of links
     *
     * @param weight     Integer (limit) of all links from source node
     * @param sourceNode source node name
     * @return all nodes excluding source node
     */
    public synchronized Set<String> getCloserThan(int weight, String sourceNode) {
        LOGGER.info("Calculating closer than");
        if (null == getNodes().get(sourceNode)) {
            return null;
        }
        AlgorithmImplementation dijkstra = new AlgorithmImplementation(DIRECTED_GRAPH_INSTANCE);
        dijkstra.execute(getNodes().get(sourceNode));
        return dijkstra.getCloserThan(weight, sourceNode);
    }

    /**
     * @return all nodes in a HashMap
     */
    public synchronized HashMap<String, String> getNodes() {
        return nodes;
    }

    /**
     * @return list with all edges (sourceNode, destinationNode, weight)
     */
    CopyOnWriteArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Singleton instance of DirectedGraph class to be shared among multiple sessions
     *
     * @return DirectedGraph INSTANCE
     */
    public static DirectedGraph getInstance() {
        if (null == DIRECTED_GRAPH_INSTANCE) {
            DIRECTED_GRAPH_INSTANCE = new DirectedGraph();
        }
        return DIRECTED_GRAPH_INSTANCE;
    }
}