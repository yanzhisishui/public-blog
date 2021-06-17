package com.syc.blog.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IllegalWordsHelper {
    private static Set<String> words;
    public static Map<String,String> wordMap;
    public static int minMatchType = 1;      //最小匹配规则
    public static int maxMatchType = 3;      //最大匹配规则

    @SuppressWarnings("all")
    public static void init() throws IOException {
        //初始化敏感词库Set
        Resource resource = new ClassPathResource("words.properties");
        words=new HashSet<>();
        InputStream is = resource.getInputStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = br.readLine()) != null){
            words.add(line);
        }
        br.close();
        is.close();


        //初始化敏感词Map
        wordMap = new HashMap<>(words.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWordMap = null;
        //遍历敏感词列表
        for (String word : words) {
            key = word;    //关键词
            nowMap = wordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMapTemp = nowMap.get(keyChar);       //获取

                if (wordMapTemp != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMapTemp;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWordMap = new HashMap<>();
                    newWordMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWordMap);
                    nowMap = newWordMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }

    /**
     * 获取文字中的敏感词
     * @param txt 文字
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return
     * @version 1.0
     */
    public static Set<String> getIllegalWord(String txt , int matchType){
        Set<String> sensitiveWordList = new HashSet<String>();

        for(int i = 0 ; i < txt.length() ; i++){
            int length = checkIllegalWord(txt, i, matchType);    //判断是否包含敏感字符
            if(length > 0){    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i+length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    @SuppressWarnings({ "rawtypes"})
    public static int checkIllegalWord(String txt, int beginIndex, int matchType){
        boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = wordMap;
        for(int i = beginIndex; i < txt.length() ; i++){
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if(nowMap != null){     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;                            //结束标志位为true
                    if(minMatchType == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            }
            else{     //不存在，直接返回
                break;
            }
        }
        if(!flag){
            matchFlag = 0;
        }
        return matchFlag;
    }
}
