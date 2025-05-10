package cn.owen233666.adventurechat.utils.Cache;

import cn.owen233666.adventurechat.utils.DataType.ItemData;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ItemShowCache {

    public static LoadingCache<UUID, ItemData> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new CacheLoader<UUID, ItemData>() {
                @Override
                public ItemData load(UUID key) throws ExecutionException {
                    return cache.get(key);
                }
            });
}
