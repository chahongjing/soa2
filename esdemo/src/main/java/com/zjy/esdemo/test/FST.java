package com.zjy.esdemo.test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FST implements Serializable {
    private final Map<Character, FST> transitions = new HashMap<>();

    private boolean isFinalState = false;

    public void addWord(String word) {
        if (word.isEmpty()) {
            isFinalState = true;
            return;
        }
        char c = word.charAt(0);
        transitions.computeIfAbsent(c, key -> new FST()).addWord(word.substring(1));
    }

    public boolean containsWord(String word) {
        if (word.isEmpty()) {
            return isFinalState;
        }
        char c = word.charAt(0);
        FST nextState = transitions.get(c);
        if (nextState == null) {
            return false;
        }
        return nextState.containsWord(word.substring(1));
    }

    public boolean removeWord(String word) {
        if (word.isEmpty()) {
            boolean wasFinal = isFinalState;
            isFinalState = false;
            return wasFinal;
        }
        char c = word.charAt(0);
        FST nextState = transitions.get(c);
        if (nextState == null) {
            return false;
        }
        boolean wasRemoved = nextState.removeWord(word.substring(1));
        if (nextState.transitions.isEmpty() && !nextState.isFinalState) {
            transitions.remove(c);
        }
        return wasRemoved;
    }
}