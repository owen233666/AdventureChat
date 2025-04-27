package cn.owen233666.adventurechat.title;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.server.level.ServerPlayer;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TitleSystem {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Map<UUID, String> TITLE_CACHE = new HashMap<>();
    private static Connection dbConnection;

    // 初始化数据库连接
    public static void initDatabase(String dbPath) throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS player_titles (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "title TEXT NOT NULL)");
        }
    }

    // 设置玩家称号
    public static void setPlayerTitle(ServerPlayer player, String titleFormat) {
        UUID uuid = player.getUUID();
        TITLE_CACHE.put(uuid, titleFormat);
        try (PreparedStatement ps = dbConnection.prepareStatement(
                "INSERT OR REPLACE INTO player_titles VALUES (?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setString(2, titleFormat);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取带称号的玩家名
    public static Component getTitledName(ServerPlayer player) {
        String titleFormat = TITLE_CACHE.computeIfAbsent(player.getUUID(), uuid -> {
            try (PreparedStatement ps = dbConnection.prepareStatement(
                    "SELECT title FROM player_titles WHERE uuid = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                return rs.next() ? rs.getString("title") : "";
            } catch (SQLException e) {
                return "";
            }
        });

        if (titleFormat.isEmpty()) {
            return Component.text(player.getScoreboardName());
        }

        // 解析称号格式（支持MiniMessage）
        String formatted = titleFormat
                .replace("{player}", player.getScoreboardName());

        return MINI_MESSAGE.deserialize(formatted);
    }
}