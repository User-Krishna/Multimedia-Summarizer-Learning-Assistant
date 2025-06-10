package com.project.preprocess;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextPreprocessor {

    private SentenceModel sentenceModel;
    private TokenizerModel tokenizerModel;

    public TextPreprocessor(String sentenceModelPath, String tokenizerModelPath) throws IOException {
        sentenceModel = new SentenceModel(new FileInputStream(sentenceModelPath));
        tokenizerModel = new TokenizerModel(new FileInputStream(tokenizerModelPath));
    }

    public String[] tokenizeSentences(String text) {
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
        return sentenceDetector.sentDetect(text);
    }

    public String[] tokenizeWords(String sentence) {
        TokenizerME tokenizer = new TokenizerME(tokenizerModel);
        return tokenizer.tokenize(sentence);
    }

    public List<String> removeStopWords(String[] words, List<String> stopWords) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (!stopWords.contains(word.toLowerCase())) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }
}