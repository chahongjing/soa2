package com.zjy.esdemo.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;

import java.util.Collection;
import java.util.List;

public class CustomSmartChineseAnalyzer extends Analyzer {

    private CharArraySet extendWords;

    private Collection<String> words;

    private CharArraySet stopWords;

    public CustomSmartChineseAnalyzer(CharArraySet stopWords, Collection<String> words) {
        this.stopWords = stopWords;
        this.words = words;
    }

    @Override
    public TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer tokenizer = new ClassicTokenizer();
        TokenStream result = tokenizer;
        result = new LowerCaseFilter(result);

//        result = new PorterStemFilter(result);

        if (stopWords != null && !stopWords.isEmpty()) {
            result = new StopFilter(result, stopWords);
        }

        if (words != null && !words.isEmpty()) {
            result = new ExtendWordFilter(result, words);
        }

        return new TokenStreamComponents(tokenizer, result);
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }
}