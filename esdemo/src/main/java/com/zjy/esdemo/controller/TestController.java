package com.zjy.esdemo.controller;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentHighLevelService;
import com.zjy.esdemo.service.StudentLowLevelService;
import com.zjy.esdemo.service.StudentService;
import com.zjy.esdemo.service.impl.Constants;
import com.zjy.esdemo.service.impl.EsHighLevelOpt;
import com.zjy.esdemo.service.impl.StudentServiceImpl;
import com.zjy.esdemo.service.impl.Utils;
import com.zjy.esdemo.test.CustomSmartChineseAnalyzer;
import com.zjy.esdemo.test.MyFST;
import com.zjy.esdemo.test.FSTUtil;
import com.zjy.esdemo.test.ValueLocation;
import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    StudentService studentService;
    @Autowired
    StudentHighLevelService studentHighLevelService;
    @Autowired
    EsHighLevelOpt esHighLevelOpt;

    @Autowired
    StudentLowLevelService studentLowLevelService;


    @RequestMapping("initData")
    public String initData() {
        String initData = Utils.getJsonByTemplate("initData.json");
        List<Student> studentList = JSON.parseArray(initData, Student.class);
        studentHighLevelService.bulkInsert(Constants.STUDENT_INDEX, studentList);
        return "success";
    }

    // region starter
    @RequestMapping("findAll")
    public String findAll() {
        return JSON.toJSONString(studentService.findAll());
    }

    @RequestMapping("findByName")
    public List<Student> findByName(String name) {
        return studentService.findByName(name);
    }

    @RequestMapping("saveTest")
    public String saveTest() {
        studentService.saveTest();
        return "success";
    }

    @RequestMapping("save")
    public String save() {
        Student bean = new Student();
        bean.setStudentId(10L);
        bean.setName("曾军毅");
        bean.setAge(43);
        studentService.save(bean);
        return "success";
    }

    @RequestMapping("deleteAll")
    public String deleteAll() {
        studentService.deleteAll();
        return "success";
    }

    @RequestMapping("test")
    public String test() {
//        List<Student> list = ((StudentServiceImpl) studentService).contain("王二丫");
//        List<Student> list = ((StudentServiceImpl) studentService).contain("王二丫", PageRequest.of(0, 5));
        List<Student> list = ((StudentServiceImpl) studentService).bool("王二丫", "jkl", 25);
        return JSON.toJSONString(list);
    }
    // endregion

    // region highlevel
    @RequestMapping("createIndex")
    public boolean createIndex(String index) {
        return studentHighLevelService.createIndex(index);
//        return studentService.createIndex();
    }

    @RequestMapping("deleteIndex")
    public boolean deleteIndex(String index) {
        return studentHighLevelService.deleteIndex(index);
    }

    @RequestMapping("insert")
    public String insert() {
        studentHighLevelService.insertTestDoc(Constants.STUDENT_INDEX);
        return "success";
    }

    @RequestMapping("batchInsertDoc")
    public String batchInsertDoc() {
        studentHighLevelService.batchInsertTestDoc(Constants.STUDENT_INDEX);
        return "success";
    }

    @RequestMapping("update")
    public String update(long id, int age) {
        studentHighLevelService.update(Constants.STUDENT_INDEX, id, age);
        return "success";
    }

    @RequestMapping("delete")
    public String delete(String id) {
        studentHighLevelService.deleteDoc(Constants.STUDENT_INDEX, id);
        return "success";
    }

    @RequestMapping("deleteByQuery")
    public String deleteByQuery() {
        esHighLevelOpt.deleteByQuery();
        return "success";
    }

    @RequestMapping("get")
    public Student get() {
        return studentHighLevelService.get(5L);
    }

    @RequestMapping("search")
    public List<Student> search() {
        return studentHighLevelService.search();
    }

    @RequestMapping("es")
    public Object es() {
        List<String> ids = new ArrayList<>();
        ids.add("2");
        ids.add("6");
        ids.add("8");
        return studentHighLevelService.getList(Constants.STUDENT_INDEX, ids);
    }

    @RequestMapping("createEsTemplate")
    public String createEsTemplate() {
        return esHighLevelOpt.createEsTemplate(Constants.STUDENT_SEARCH_TEMPLATE);
    }

    @RequestMapping("deleteEsTemplate")
    public String deleteEsTemplate() {
        return esHighLevelOpt.deleteEsTemplate(Constants.STUDENT_SEARCH_TEMPLATE);
    }

    @RequestMapping("getEsTemplate")
    public String getEsTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王二丫");
        map.put("gt", 20);
        map.put("lt", 40);
        return esHighLevelOpt.getEsTemplate(Constants.STUDENT_SEARCH_TEMPLATE, map);
    }

    @RequestMapping("searchByTemplate")
    public String searchByTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王二丫");
        map.put("gt", 20);
        map.put("lt", 40);
        List<Map<String, Object>> maps = esHighLevelOpt.useEsTemplate(Constants.STUDENT_INDEX, Constants.STUDENT_SEARCH_TEMPLATE, map);
        return JSON.toJSONString(maps);
    }

    @RequestMapping("suggest")
    public List<String> suggest(String keyword) {
        return esHighLevelOpt.suggest(Constants.STUDENT_INDEX, keyword);
    }
    // endregion

    // region lowlevel
    @RequestMapping("queryList")
    public String queryList() {
        return studentLowLevelService.queryList();
    }
    // endregion

    public static void main(String[] arg) throws IOException {
        test1();
        test2();
    }

    private static void test1() {
        MyFST dict = new MyFST();
        for (String s : getKeyword()) {
            dict.addWord(s);
        }
        long ct = System.currentTimeMillis();
        List<ValueLocation> result2 = FSTUtil.parse(getContent(), dict, 0, 5);
        long duration = System.currentTimeMillis() - ct;
        Set<String> set = new HashSet<>();
        for (ValueLocation term : result2) {
            set.add(term.getText());
        }
        log.info("time:{},result:{}", duration, StringUtils.join(set, "|"));
    }

    private static void test2() {
        final Collection<String> keywords = getKeyword();
        final String content = getContent();
        final long ct = System.currentTimeMillis();
        Trie trie = Trie.builder().addKeywords(keywords).build();
        Collection<Emit> emits = trie.parseText(content);
        long duration = System.currentTimeMillis() - ct;
        Set<String> set = new HashSet<>();
        for (Emit emit : emits) {
            set.add(emit.getKeyword());
        }
        log.info("keyword size:{},content length:{},time:{},match:{}", keywords.size(), content.length(), duration,
                JSON.toJSONString(set));
    }

    private static void test3() {
        MyFST dict = new MyFST();
        String text = "试一试分词效果，我得名字叫彭胜文，曾用名是彭1胜利";
        dict.addWord("文，曾用名");
        dict.addWord("彭胜文");
        dict.addWord("彭胜");
        dict.addWord("果，我得");
        dict.addWord("细菌");
        long ct = System.currentTimeMillis();
        List<ValueLocation> result = FSTUtil.parse(text, dict, 2, 5);
        long duration = System.currentTimeMillis() - ct;
        List<String> res = new ArrayList<>();
        for (ValueLocation term : result) {
            res.add(term.getText());
        }
        log.info("time:{},result:{}", duration, StringUtils.join(res, "|"));

        dict.removeWord("彭胜文");
        dict.removeWord("彭胜");
        res.clear();

        ct = System.currentTimeMillis();
        result = FSTUtil.parse(text, dict);
        duration = System.currentTimeMillis() - ct;
        for (ValueLocation term : result) {
            res.add(term.getText());
        }
        log.info("time:{},result:{}", duration, StringUtils.join(res, "|"));

    }

    private static void test4() throws IOException {
        Analyzer analyzer = new CustomSmartChineseAnalyzer(null, getKeyword());
        long ct = System.currentTimeMillis();
        TokenStream tokenStream = analyzer.tokenStream("", getContent());
        long duration = System.currentTimeMillis() - ct;

        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        tokenStream.reset();
        List<String> tokens = new ArrayList<>();
        while (tokenStream.incrementToken()) {
            tokens.add(offsetAttribute.toString());
        }
        tokenStream.end();
        log.info("time:{},tokens:{}", duration, StringUtils.join(tokens, "|"));

//        StringReader input = new StringReader(text.trim());
//        // true 用智能分词/false细粒度
//        IKSegmenter ikSeg = new IKSegmenter(input, true);
//        Lexeme lexeme = ikSeg.next();
//        for (; lexeme != null; lexeme = ikSeg.next()) {
//            // 禁用默认词典，只用自定义词典
//            // 1.默认词典设为停用词典
//            // 2.getLexemeType为64的直接跳过
//            int nType = lexeme.getLexemeType();
//            if (nType == 64) {
//                continue;
//            }
//            System.out.print(lexeme.getLexemeText() + " ");
//        }
    }

    private static Collection<String> getKeyword() {
        final String keyStr;
        try {
            keyStr = FileUtils.readFileToString(new File("c:/Users/zengjunyi/Desktop/a.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.stream(keyStr.split(",")).collect(Collectors.toList());
    }

    private static String getContent() {
        try {
            return FileUtils.readFileToString(new File("c:/Users/zengjunyi/Desktop/a2.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
