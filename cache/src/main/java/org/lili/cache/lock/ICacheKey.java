package org.lili.cache.lock;


public interface ICacheKey {

    String SEPARATE = ":";
    int DEFAULT_EXPIRE = 60 * 60 * 1000;

    /**
     * cache key对key统一管理
     *
     * @return Key
     */
    String getKey();

    /**
     * key的过期时间 毫秒值
     *
     * @return expire time
     */
    int getExpirationTime();
}
