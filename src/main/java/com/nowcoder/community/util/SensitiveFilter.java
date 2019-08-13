package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 托管给容器，为了复用方便，在各个层次都可以用。
 */
@Component
public class SensitiveFilter {
    /**
     * 定义前缀树
     * 根据敏感词，初始化前缀树
     * 编写过滤敏感词的方法
     */
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    // 替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    //TODO：定义(描述前缀树的节点)
    private class TrieNode{
        //TODO：关键词结束标志
        private boolean isKeyWordEnd = false;

        private boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean isKeyWordEnd) {
            this.isKeyWordEnd = isKeyWordEnd;
        }
        //TODO：当前节点的子节点，可能有多个
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        //TODO：添加子节点（c为子节点的key，node为value）
        public void addsubNode(Character c, TrieNode node) {
            subNodes.put(c,node);
        }
        //TODO：获取子节点
        public TrieNode getsubNode(Character c) {
            return subNodes.get(c);
        }
    }
    //TODO：根据数据，初始化前缀树

    /**
     * 在服务启动的时候被初始化
     */
    @PostConstruct
    public void init() {
        try (
                //从类路径下去读文件
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }
    //TODO：做成树的上下引用关系，把keyword添加到前缀树
    private void addKeyword(String keyword) {
        TrieNode curNode = rootNode;
        //TODO：遍历单词的字符，从根节点开始，判断当前节点是否有当前字符的子节点，如果没有，那就把字符挂到当前节点的下面，
        for (int i = 0; i < keyword.length(); i++) {
            TrieNode node = curNode.subNodes.get(keyword.charAt(i));
            if (node == null) {
                //初始化子节点
                node = new TrieNode();
                curNode.subNodes.put(keyword.charAt(i), node);
            }
            //TODO：指向子节点，进入下一轮循环
            curNode = node;
            //TODO：设置结束标志
            if (i ==keyword.length() - 1) {
                curNode.setKeyWordEnd(true);
            }
        }
    }
    //TODO:编写过滤敏感词的方法

    /**
     *  过滤敏感词
     * @param text 带过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        /**
         * 使用3个指针，一个StringBuilder来记录结果
         */
        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getsubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }
    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }
}
