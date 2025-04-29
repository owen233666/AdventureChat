package cn.owen233666.adventurechat.utils;

import java.util.regex.*;

public class convertHexColorCodes {
    public static String convertColorCodes(String input) {
        // 正则表达式匹配 &# 后面跟着6个十六进制字符
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(input);

        // 使用StringBuffer来构建结果
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            // 获取匹配的完整颜色代码（包括&#）
            String fullMatch = matcher.group(0);
            // 获取颜色值部分（去掉&#）
            String colorValue = matcher.group(1);
            // 构建替换字符串
            String replacement = "<#" + colorValue + ">";
            // 替换并追加到结果中
            matcher.appendReplacement(sb, replacement);
        }
        // 追加剩余部分
        matcher.appendTail(sb);

        return sb.toString();
    }
}
