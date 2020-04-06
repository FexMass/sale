package org.collibra.challenge.algorithm;

class Edge {
    private final String sourceNode;
    private final String destinationNode;
    private final int weight;

    Edge(String sourceNode, String destinationNode, int weight) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.weight = weight;
    }

    String getDestinationNode() {
        return destinationNode;
    }

    String getSourceNode() {
        return sourceNode;
    }
    int getWeight() {
        return weight;
    }
}