package cn.owen233666.adventurechat.database;

import cn.owen233666.adventurechat.AdventureChat;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AdventureChatDatabase {
    private static final String DB_NAME = AdventureChat.MODID + ".db";
    private static final String TABLE_NAME = "player_title";
    private static Path dbPath;
    public static void init() {
        dbPath = Paths.get("config/AdventureChat", DB_NAME);
    }
}
