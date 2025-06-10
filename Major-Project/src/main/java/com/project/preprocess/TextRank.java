package com.project.preprocess;

import java.util.*;

public class TextRank {

    public List<String> rankSentences(List<String> sentences) {
        // Create a graph of sentences
        Map<String, Set<String>> sentenceGraph = new HashMap<>();
        for (String sentence : sentences) {
            sentenceGraph.putIfAbsent(sentence, new HashSet<>());
        }

        // Calculate sentence similarity and build edges
        for (int i = 0; i < sentences.size(); i++) {
            for (int j = i + 1; j < sentences.size(); j++) {
                double similarity = calculateSimilarity(sentences.get(i), sentences.get(j));
                if (similarity > 0.1) {
                    sentenceGraph.get(sentences.get(i)).add(sentences.get(j));
                    sentenceGraph.get(sentences.get(j)).add(sentences.get(i));
                }
            }
        }

        // Apply TextRank algorithm
        final Map<String, Double> scores = new HashMap<>();
        for (String sentence : sentences) {
            scores.put(sentence, 1.0);
        }

        for (int iter = 0; iter < 100; iter++) {
            final Map<String, Double> newScores = new HashMap<>();
            for (String sentence : sentences) {
                double score = 1 - 0.85;
                for (String neighbor : sentenceGraph.get(sentence)) {
                    score += 0.85 * (scores.get(neighbor) / sentenceGraph.get(neighbor).size());
                }
                newScores.put(sentence, score);
            }
            scores.clear();
            scores.putAll(newScores);
        }

        // Sort sentences by score
        List<String> rankedSentences = new ArrayList<>(sentences);
        rankedSentences.sort((s1, s2) -> Double.compare(scores.get(s2), scores.get(s1)));
        return rankedSentences;
    }

    private double calculateSimilarity(String sentence1, String sentence2) {
        Set<String> words1 = new HashSet<>(Arrays.asList(sentence1.split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(sentence2.split("\\s+")));

        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        return (double) intersection.size() / (Math.sqrt(words1.size()) * Math.sqrt(words2.size()));
    }
}