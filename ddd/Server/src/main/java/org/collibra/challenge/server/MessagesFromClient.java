package org.collibra.challenge.server;

import org.collibra.challenge.algorithm.DirectedGraph;

import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class represent commands logic with validation to be executed
 */
class MessagesFromClient {

    private static DirectedGraph directedGraphInstance = DirectedGraph.getInstance();
    private static final Logger LOGGER = Logger.getLogger(MessagesFromClient.class.getName());
    private static final String UNRECOGNIZED_WORD = "SORRY, I DIDN'T UNDERSTAND THAT";

    /**
     * Method to handle all Commands and execute proper method, otherwise fault message is returned
     *
     * @return response for Client
     */
    static String processResponse(String inputMessage) {
        if (null == inputMessage || inputMessage.contains("BYE MATE!")) {
           return "";
        }

        try {
            LOGGER.info("Client: " + inputMessage);
            //regex validation of hardcoded commands which are valid
            String inputValidation = ".*ADD NODE.*|.*REMOVE NODE.*|.*ADD EDGE.*|.*REMOVE EDGE.*|.*SHORTEST PATH.*|.*CLOSER THAN.*";
            //default message is return if message is not matching validation
            if (!Pattern.matches(inputValidation, inputMessage)) {
                LOGGER.info("Validation failed for message: " + inputMessage);
                return UNRECOGNIZED_WORD;
            } else {
                //splitting the word by spaces to get values provided after command
                String[] splitInputWord = inputMessage.split(" ");
                //length of splitInputWord to validated afterwards
                int splitInputWordLength = splitInputWord.length;

                //logic for adding node with argument (node name)
                if (inputMessage.startsWith("ADD NODE") && (splitInputWordLength == 3)) {
                    return addNode(splitInputWord[2]);
                }
                //logic for removing node with argument (node name)
                if (inputMessage.startsWith("REMOVE NODE") && (splitInputWordLength == 3)) {
                    return removeNode(splitInputWord[2]);
                }
                //logic for adding edge with arguments (source node name, destination node name, weight)
                if (inputMessage.startsWith("ADD EDGE") && (splitInputWordLength == 5)) {
                    return addEdge(splitInputWord[2], splitInputWord[3], Integer.parseInt(splitInputWord[4]));
                }
                //logic for removing edge with arguments (source node name, destination node name)
                if (inputMessage.startsWith("REMOVE EDGE") && (splitInputWordLength == 4)) {
                    return removeEdge(splitInputWord[2], splitInputWord[3]);
                }
                //logic for finding shortest path with arguments (source node name, destination node name)
                if (inputMessage.startsWith("SHORTEST PATH") && (splitInputWordLength == 4)) {
                    return String.valueOf(shortestPath(splitInputWord[2], splitInputWord[3]));
                }
                //logic for finding closer than with arguments (weight, destination node name)
                if (inputMessage.startsWith("CLOSER THAN") && (splitInputWordLength == 4)) {
                    return closerThan(Integer.parseInt(splitInputWord[2]), splitInputWord[3]);
                }
            }
        } catch (Exception e) {
            LOGGER.info("Validation failed for message: " + inputMessage);
            return UNRECOGNIZED_WORD;
        }
        LOGGER.info("Validation failed for message: " + inputMessage);
        return UNRECOGNIZED_WORD;
    }

    /**
     * @param nodeName String word to be added as node name
     * @return message if the operation is successful
     */
    private static String addNode(String nodeName) {
        return directedGraphInstance.addNode(nodeName) ? "NODE ADDED" : "ERROR: NODE ALREADY EXISTS";
    }

    /**
     * @param sourceNode      source node name
     * @param destinationNode destination node name
     * @param weight          Integer value (weight) of link between two nodes
     * @return message if the operation is successful
     */
    private static String addEdge(String sourceNode, String destinationNode, int weight) {
        return directedGraphInstance.addEdge(
                directedGraphInstance.getNodes().get(sourceNode),
                directedGraphInstance.getNodes().get(destinationNode),
                weight) ? "EDGE ADDED" : "ERROR: NODE NOT FOUND";
    }

    /**
     * @param nodeName node name
     * @return message if the operation is successful
     */
    private static String removeNode(String nodeName) {
        return directedGraphInstance.removeNode(nodeName) ? "NODE REMOVED" : "ERROR: NODE NOT FOUND";
    }

    /**
     * @param sourceNode      source node name
     * @param destinationNode destination node name
     * @return message if the operation is successful
     */
    private static String removeEdge(String sourceNode, String destinationNode) {
        return directedGraphInstance.removeEdge(sourceNode, destinationNode) ? "EDGE REMOVED" : "ERROR: NODE NOT FOUND";
    }

    /**
     * @param sourceNode      source node name
     * @param destinationNode destination node name
     * @return shortest path between provided nodes (number)
     */
    private static String shortestPath(String sourceNode, String destinationNode) {
        String result = String.valueOf(directedGraphInstance.getShortestPath(sourceNode, destinationNode));
        return  result.equals("0") ? "ERROR: NODE NOT FOUND" : result;
    }

    /**
     * @param weight     combined weight of links (number)
     * @param sourceNode source node name
     * @return comma separated list of the found nodes, sorted alphabetically by name
     */
    private static String closerThan(int weight, String sourceNode) {
        Set<String> result = directedGraphInstance.getCloserThan(weight, sourceNode);
        return null == result ? "ERROR: NODE NOT FOUND" : result.toString().replaceAll("[\\[\\]\\s]", "");
    }
}