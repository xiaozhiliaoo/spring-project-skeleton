package org.lili.cache.lock;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import static org.lili.cache.lock.ICacheKey.SEPARATE;


public class RedisLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);
    private static final String NX = "NX";
    private static final String PX = "PX";
    private static final String LOCK_SUCCESS_CODE = "OK";
    private static final Integer UNLOCK_SUCCESS_CODE = 1;

    /**
     * lua脚本：判断锁住值是否为当前线程持有，是的话解锁，不是的话解锁失败
     */
    private static final String UNLOCK_SCRIPT = ""
            + " local v = redis.call('get', KEYS[1]);"
            + " if (type(v) == 'boolean') "
            + " then return 0"
            + " else "
            + " v = string.match(v, ARGV[1])"
            + " if (v ~= nil and string.len(v) > 0)"
            + " then  return redis.call('del', KEYS[1]) "
            + " else  return 0"
            + " end"
            + " end";


    private JedisCluster jedisCluster;
    private ICacheKey cacheKey;
    private LockEnv lockEnv;

    public RedisLock(JedisCluster jedisCluster, ICacheKey cacheKey, LockEnv lockEnv) {
        this.jedisCluster = jedisCluster;
        this.cacheKey = cacheKey;
        this.lockEnv = lockEnv;
    }


    /**
     * 尝试获取锁;
     *
     * @param value value
     * @return 成功返回:true ;失败返回false
     */
    public boolean lock(String value) {
        try {
            String result = jedisCluster.set(cacheKey.getKey(), value, NX, PX, cacheKey.getExpirationTime());
            logger.info("lock result={}; lockKey={}; lockValue={} ;", result, cacheKey.getKey(), value);
            return LOCK_SUCCESS_CODE.equals(result);
        } catch (Exception e) {
            logger.error("获取锁时出现异常： " + e.getMessage() + SEPARATE + cacheKey.getKey() + SEPARATE + value);
            throw e;
        }
    }


    /**
     * 释放锁
     *
     * @param value value
     * @return 释放成功 返回true ||释放失败返回false
     */
    public boolean unLock(String value) {
        try {
            Integer result = Integer.parseInt(String.valueOf(jedisCluster.eval(UNLOCK_SCRIPT, 1, cacheKey.getKey(), value)));
            logger.info("unlock result={}; lockKey={}; lockValue={} ", result, cacheKey.getKey(), value);
            return UNLOCK_SUCCESS_CODE.equals(result);
        } catch (Exception e) {
            logger.error("释放锁时失败出现异常 ： " + e.getMessage() + SEPARATE + cacheKey.getKey() + SEPARATE + value);
            return false;
        }
    }


    /**
     * 重复获取lock 10次，每次间隔600毫秒
     *
     * @return 是否获得锁
     */
    public boolean tryLock() {
        int retryTimes = 10;
        while (retryTimes > 0) {
            if (lock(generateLockValue(lockEnv))) {
                return true;
            }

            retryTimes--;

            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                logger.error("尝试重复获取锁时被中断sleep异常 ： " + e.getMessage() + SEPARATE + cacheKey.getKey());
            }
        }
        logger.info("重复10次未获取到锁; cacheKey={}; app={}; port={}; ip={};", cacheKey.getKey(), lockEnv.getApplicationName(), lockEnv.getPort(), lockEnv.getIp());
        return false;
    }


    /**
     * 释放锁
     *
     * @return 释放成功 返回true ||释放失败返回false
     */
    public boolean unLock() {
        try {
            Integer result = Integer.parseInt(String.valueOf(jedisCluster.eval(UNLOCK_SCRIPT, 1, cacheKey.getKey(), generateUnLockValue(lockEnv))));
            logger.info("unlock result : {} ; lockKey : {} ; app={}; port={}; ip={};", result, cacheKey.getKey(), lockEnv.getApplicationName(), lockEnv.getPort(), lockEnv.getIp());
            return UNLOCK_SUCCESS_CODE.equals(result);
        } catch (Exception e) {
            logger.info("释放锁时失败出现异常; cacheKey={}; app={}; port={}; ip={};", cacheKey.getKey(), lockEnv.getApplicationName(), lockEnv.getPort(), lockEnv.getIp());
            logger.error("释放锁时失败出现异常 ： " + e.getMessage() + SEPARATE + cacheKey.getKey(), e);
            return false;
        }
    }


    private String generateLockValue(LockEnv lockEnv) {
        StringBuilder sb = new StringBuilder();
        sb.append(lockEnv.getIp().replaceAll("-", ""))
                .append(SEPARATE)
                .append(Thread.currentThread().getId())
                .append(SEPARATE)
                .append(lockEnv.getPort())
                .append(SEPARATE)
                .append(lockEnv.getApplicationName())
                .append(SEPARATE)
                .append(System.currentTimeMillis());

        return sb.toString();
    }

    private String generateUnLockValue(LockEnv lockEnv) {
        StringBuilder sb = new StringBuilder();
        sb.append(lockEnv.getIp().replaceAll("-", ""))
                .append(SEPARATE)
                .append(Thread.currentThread().getId())
                .append(SEPARATE)
                .append(lockEnv.getPort())
                .append(SEPARATE);

        return sb.toString();
    }
}
