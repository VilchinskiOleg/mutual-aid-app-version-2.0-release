package org.tms.common.auth.configuration.global.algorithms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import static java.util.Collections.reverse;
import static java.util.Objects.nonNull;

public class ValuableGraphSearcher {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {
        private String name;
        private int valueToThisNode;
        private List<Node> nextNodes;
    }

    @Getter
    @AllArgsConstructor
    public class ResultTableEntry {
        private String destination;
        private String parent;
        private int value;

        @Override
        public String toString() {
            return String.format("%s -> %s", parent, destination);
        }
    }

    private Map<String, ResultTableEntry> resultTable = new HashMap<>();


    public List<ResultTableEntry> searchShortestWay(Node root, String finalDestinationNode) {
        // init queue:
        Queue<Node> currentNodes = new ArrayDeque<>();
        currentNodes.add(root);
        
        // calculate total values for every node:
        while (!currentNodes.isEmpty()) {
            Node currentNode = currentNodes.poll();
            refreshDestinationToAllNaigburs(currentNode);
            currentNodes.addAll(currentNode.nextNodes);
        }

        // retrieve result root:
        return retrieveResult(finalDestinationNode);
    }

    /**
     * Retrieve and refresh destianation (value) from current node to all its naigburs.
     */
    private void refreshDestinationToAllNaigburs(Node currentNode) {
        for (ValuableGraphSearcher.Node next : currentNode.nextNodes) {
            var storedParentData = resultTable.get(currentNode.getName());
            int totalValue = next.getValueToThisNode() + (nonNull(storedParentData) ? storedParentData.getValue() : 0);
            analiseNextNode(next.getName(), currentNode.getName(), totalValue);
        }
    }

    /**
     * Retrieve and change value for some particular naigbur.
     * 
     * @param destination
     * @param parent
     * @param totalValue
     */
    private void analiseNextNode(String destination , String parent, int totalValue) {
        if (!resultTable.containsKey(destination) || resultTable.get(destination).getValue() > totalValue) {
            resultTable.put(destination, new ResultTableEntry(destination, parent, totalValue));
        }
    }

    /**
     * retrieve result root.
     */
    private List<ResultTableEntry> retrieveResult(String finalDestinationNode) {
        var destination = resultTable.get(finalDestinationNode);
        if (destination == null) throw new IllegalArgumentException("Don't have such destination node in result table!");
        List<ValuableGraphSearcher.ResultTableEntry> resultWay = new ArrayList<>();
        while (nonNull(destination)) {
            resultWay.add(destination);
            destination = resultTable.get(destination.getParent());
        }
        reverse(resultWay);
        return resultWay;
    }
}