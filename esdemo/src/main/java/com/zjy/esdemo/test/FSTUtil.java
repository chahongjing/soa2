package com.zjy.esdemo.test;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.Util;

import java.util.ArrayList;
import java.util.List;

public class FSTUtil {

    /**
     * 最大匹配，并且额外返回了字典在文本中所处的位置。
     *
     * @param text
     * @param dict
     * @return
     */
    public static List<ValueLocation> parse(String text, FST dict) {
        return parse(text, dict, 1, text.length());
    }

    public static List<ValueLocation> parse(String text, FST dict, int minLength, int maxLength) {
        List<ValueLocation> result = new ArrayList<>();
        // 向后推进，如内容：我是中国人。 关键词：中国
        // 1：我是中国人  2：是中国人  3：中国人
        for (int start = 0; start < text.length(); start++) {
            int end = Math.min(text.length(), maxLength + start);
            // 字符串从大到小切分判断，缩短字符串
            for (; end >= start + minLength; end--) {
                String word = text.substring(start, end);
                if (dict.containsWord(word)) {
                    result.add(new ValueLocation(word, start, end));
                }
            }
        }
        return result;
    }

    public static void main(String[] arg) {
        String[] inputValues = {"cat", "deep", "do", "dog", "dogs"};
        long outputValues[] = {5, 7, 17, 18, 21};
        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<>(org.apache.lucene.util.fst.FST.INPUT_TYPE.BYTE1, outputs);
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        try {
            for (int i = 0; i < inputValues.length; i++) {
                builder.add(Util.toIntsRef(new BytesRef(inputValues[i]), scratchInts), outputValues[i]);
//                builder.add(Util.toUTF16(inputValues[i], scratchInts), outputValues[i]);
            }
            org.apache.lucene.util.fst.FST<Long> fst = builder.finish();
            Long value = Util.get(fst, new BytesRef("dog"));
            System.out.println("value: " + value);
        } catch (Exception ex) {

        }
    }
}
