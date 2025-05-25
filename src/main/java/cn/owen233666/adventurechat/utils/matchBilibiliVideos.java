package cn.owen233666.adventurechat.utils;

import net.kyori.adventure.text.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class matchBilibiliVideos {
    public static String bilibilimatcher(String input) {
        Pattern bilipattern = Pattern.compile("BV( )?.{10}");
        Matcher bilimatcher = bilipattern.matcher(input.toString());
        StringBuffer sb = new StringBuffer();
        if (bilimatcher.find()) {// 将匹配到的内容替换为空
            String bv = bilimatcher.group(0);
            String bilibili_url = "https://www.bilibili.com/video/" + bv;
            String click = "<hover:show_text:'这是一个<#FE65A6>Bilibili<reset>视频链接！<newline>点击查看'><click:open_url:"+bilibili_url+">"+"[<bold><#FE65A6>Bilibili<reset>]"+"<reset>";bilimatcher.appendReplacement(sb, click);
            return sb.toString();
        }
        else{
            return input;
        }
    }
}
