package cn.owen233666.adventurechat.utils.Cache;

import cn.owen233666.adventurechat.utils.DataType.EnderChestData;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class EnderChestShowCache {
    public static LoadingCache<UUID, EnderChestData> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<UUID, EnderChestData>() {
                @Override
                public EnderChestData load(UUID key) throws ExecutionException {
                    return cache.get(key);
                }
            });
}
