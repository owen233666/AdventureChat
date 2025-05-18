package cn.owen233666.adventurechat.utils.Cache;

import cn.owen233666.adventurechat.utils.DataType.InventoryData;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class InventoryShowCache {
    public static LoadingCache<UUID, InventoryData> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<UUID, InventoryData>() {
                @Override
                public InventoryData load(UUID key) throws ExecutionException {
                    return cache.get(key);
                }
            });
}
