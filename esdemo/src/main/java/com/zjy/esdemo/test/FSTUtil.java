package com.zjy.esdemo.test;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.BytesRefFSTEnum;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FSTUtil {

    /**
     * 最大匹配，并且额外返回了字典在文本中所处的位置。
     *
     * @param text
     * @param dict
     * @return
     */
    public static List<ValueLocation> parse(String text, MyFST dict) {
        return parse(text, dict, 1, text.length());
    }

    public static List<ValueLocation> parse(String text, MyFST dict, int minLength, int maxLength) {
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
        try {
            fst2();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fst1() {
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

    private static void fst2() throws IOException {
        // Input values (keys). These must be provided to Builder in Unicode sorted order!
        String inputValues[] = {"cat", "dogs", "dog", "doeverything"};
        long outputValues[] = {1,2,3,4};

        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<>(FST.INPUT_TYPE.BYTE1, outputs);
        BytesRefBuilder scratchBytes = new BytesRefBuilder();
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            scratchBytes.copyChars(inputValues[i]);
            BytesRef bytesRef = scratchBytes.get();
            builder.add(Util.toIntsRef(bytesRef, scratchInts), outputValues[i]);
        }
        FST<Long> fst = builder.finish();


        Long value = Util.get(fst, new BytesRef("cat"));
        System.out.println("Retrieval by key cat: value = " + value); // 5

        // Only works because outputs are also in sorted order
        IntsRef key = Util.getByOutput(fst, 3);
        System.out.println("Retrieval by value 12: result = " + Util.toBytesRef(key, scratchBytes).utf8ToString()); // dogs

        // Like TermsEnum, this also supports seeking (advance)
        BytesRefFSTEnum<Long> iterator = new BytesRefFSTEnum<>(fst);
        while (iterator.next() != null) {
            BytesRefFSTEnum.InputOutput<Long> mapEntry = iterator.current();
            System.out.println("terate over key-value pairs in sorted order: key = " + mapEntry.input.utf8ToString());
            System.out.println("terate over key-value pairs in sorted order: value = " + mapEntry.output);
        }

        System.out.println("top N");
        FST.Arc<Long> firstArc = fst.getFirstArc(new FST.Arc<>());
        firstArc.copyFrom(getSpecArc(fst));

        // 从"do"位置开始找走到终止状态最近的2条路径
        Util.TopResults<Long> results = Util.shortestPaths(fst, firstArc, PositiveIntOutputs.getSingleton().getNoOutput(),
                Long::compareTo, 2, false);
        Iterator<Util.Result<Long>> iterator1 = results.iterator();
        while (iterator1.hasNext()) {
            Util.Result<Long> next = iterator1.next();
            System.out.println("N-shortest paths by weight: key = " + Util.toBytesRef(next.input, scratchBytes).utf8ToString()
                    + " suffix:" + getStringPrefix(next.input, 2)); // cat
            System.out.println("N-shortest paths by weight: value = " + next.output); // 5
        }
    }

    private static FST.Arc<Long> getSpecArc(FST<Long> fst) {
        try {
            final BytesRef input = new BytesRef("do");
            final FST.BytesReader fstReader = fst.getBytesReader();

            final FST.Arc<Long> arc = fst.getFirstArc(new FST.Arc<>());

            // Accumulate output as we go
            for (int i = 0; i < input.length; i++) {
                if (fst.findTargetArc(input.bytes[i + input.offset] & 0xFF, arc, arc, fstReader) == null) {
                    return null;
                }
            }
            return arc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getStringPrefix(IntsRef input, int length) {
        int[] ints = input.ints;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ints.length; ++i) {
            if (length <= i) {
                break;
            }
            if (ints[i] > 0) {
                char valueC = (char) (ints[i] - 48 + '0');
                stringBuilder.append(valueC);
            }

        }
        return stringBuilder.toString();
    }
}
