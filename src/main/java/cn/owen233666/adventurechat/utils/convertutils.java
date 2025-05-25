package cn.owen233666.adventurechat.utils;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class convertutils {

    public static String convert(@NotNull String input){
        return convertHexColorCodes(convertVanilliaColorCodes(input));
    }

    public static String convertHexColorCodes(String input) {
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

    public static String convertVanilliaColorCodes(@NotNull String input){

        if(Pattern.compile("<g:([^:>]+):([^>]+)>").matcher(input).find()){
            Pattern pattern = Pattern.compile("<g:([^:>]+):([^>]+)>");
            return getString(input, pattern);
        } else if (Pattern.compile("<gradient:([^:>]+):([^>]+)>").matcher(input).find()) {
            input = input.replace("&a","green");
            input = input.replace("&b","aqua");
            input = input.replace("&c","red");
            input = input.replace("&d","light_purple");
            input = input.replace("&e","gold");
            input = input.replace("&f","white");
            input = input.replace("&0","black");
            input = input.replace("&1","dark_blue");
            input = input.replace("&2","dark_green");
            input = input.replace("&3","dark_aqua");
            input = input.replace("&4","dark_red");
            input = input.replace("&5","dark_purple");
            input = input.replace("&6","yellow");
            input = input.replace("&7","grey");
            input = input.replace("&8","dark_grey");
            input = input.replace("&9","blue");
            input = input.replace("&l","bold");
            input = input.replace("&r","<reset>");
            return input;
        } else{
            input = input.replace("&a","<green>");
            input = input.replace("&b","<aqua>");
            input = input.replace("&c","<red>");
            input = input.replace("&d","<light_purple>");
            input = input.replace("&e","<gold>");
            input = input.replace("&f","<white>");
            input = input.replace("&0","<black>");
            input = input.replace("&1","<dark_blue>");
            input = input.replace("&2","<dark_green>");
            input = input.replace("&3","<dark_aqua>");
            input = input.replace("&4","<dark_red>");
            input = input.replace("&5","<dark_purple>");
            input = input.replace("&6","<yellow>");
            input = input.replace("&7","<grey>");
            input = input.replace("&8","<dark_grey>");
            input = input.replace("&9","<blue>");
            input = input.replace("&l","<bold>");
            input = input.replace("&o","<italic>");
            input = input.replace("&n","<underlined>");
            input = input.replace("&m","<st>");
            input = input.replace("&k","<obf>");
            input = input.replace("&r","<reset>");
            return input;
        }
    }

    private static @NotNull String getString(@NotNull String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        StringBuffer temp = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(temp, "<gradient:" + matcher.group(1) + ":" + matcher.group(2) + ">");
        }
        String result = matcher.appendTail(temp).toString();
        result = result.replace("&a","green");
        result = result.replace("&b","aqua");
        result = result.replace("&c","red");
        result = result.replace("&d","light_purple");
        result = result.replace("&e","gold");
        result = result.replace("&f","white");
        result = result.replace("&0","black");
        result = result.replace("&1","dark_blue");
        result = result.replace("&2","dark_green");
        result = result.replace("&3","dark_aqua");
        result = result.replace("&4","dark_red");
        result = result.replace("&5","dark_purple");
        result = result.replace("&6","yellow");
        result = result.replace("&7","grey");
        result = result.replace("&8","dark_grey");
        result = result.replace("&9","blue");
        result = result.replace("&r","<reset>");
        return result;
    }
}
