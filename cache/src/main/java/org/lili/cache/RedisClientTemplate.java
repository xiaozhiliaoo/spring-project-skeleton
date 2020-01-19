package org.lili.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RedisClientTemplate {
	private static Logger log = LoggerFactory.getLogger(RedisClientTemplate.class);

	@Autowired
	private JedisCluster jedisCluster;

	private static final Map<String, Map<String, String>> LocalCache = new ConcurrentHashMap<>();

	public static String local_hget(String key, String field) {
		Map<String, String> map = LocalCache.get(key);
		if(map!=null) {
			return map.get(field);
		}
		return null;
	}

	public static Map<String, String> local_hgetAll(String key){
		return LocalCache.get(key);
	}

	public synchronized String local_hset(String key, String field, String value) {
		Map<String, String> map = LocalCache.get(key);
		if(map==null) {
			map = new ConcurrentHashMap<String, String>();
		}
		if(field==null || value==null) {
			return null;
		}
		return map.put(field, value);
	}

	public synchronized Map<String, String> local_hsetAll(String key,Map<String, String> map){
		if(key==null || map==null) {
			return null;
		}
		return LocalCache.put(key,map);
	}



	
	
	public Set<String> keys(String pattern) {
		Set<String> result = new HashSet<>();
		try {
			Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
			for (String key : clusterNodes.keySet()) {
				JedisPool jedisPool = clusterNodes.get(key);
				Jedis jedis = jedisPool.getResource();
				try {
					result.addAll(jedis.keys(pattern));
				} finally {
					jedis.close();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 设置单个key
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		String result = null;
		try {
			result = jedisCluster.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 获取单个key
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String result = null;
		try {
			result = jedisCluster.get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Boolean exists(String key) {
		Boolean result = false;
		try {
			result = jedisCluster.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
		return result;
	}

	public String type(String key) {
		String result = null;
		try {
			result = jedisCluster.type(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 在某段时间后实现
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(String key, int seconds) {
		Long result = null;
		try {
			result = jedisCluster.expire(key, seconds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 在某个时间点失效
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public Long expireAt(String key, long unixTime) {
		Long result = null;
		try {
			result = jedisCluster.expireAt(key, unixTime);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long ttl(String key) {
		Long result = null;
		try {
			result = jedisCluster.ttl(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public boolean setbit(String key, long offset, boolean value) {
		boolean result = false;
		try {
			result = jedisCluster.setbit(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public boolean getbit(String key, long offset) {
		boolean result = false;
		try {
			result = jedisCluster.getbit(key, offset);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public long setrange(String key, long offset, String value) {
		long result = 0;
		try {
			result = jedisCluster.setrange(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String getrange(String key, long startOffset, long endOffset) {
		String result = null;
		try {
			result = jedisCluster.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String getSet(String key, String value) {
		String result = null;
		try {
			result = jedisCluster.getSet(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long setnx(String key, String value) {
		Long result = null;
		try {
			result = jedisCluster.setnx(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String setex(String key, int seconds, String value) {
		String result = null;
		try {
			result = jedisCluster.setex(key, seconds, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long decrBy(String key, long integer) {
		Long result = null;
		try {
			result = jedisCluster.decrBy(key, integer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long decr(String key) {
		Long result = null;
		try {
			result = jedisCluster.decr(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long incrBy(String key, long integer) {
		Long result = null;
		try {
			result = jedisCluster.incrBy(key, integer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long incr(String key) {
		Long result = null;
		try {
			result = jedisCluster.incr(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long append(String key, String value) {
		Long result = null;
		try {
			result = jedisCluster.append(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String substr(String key, int start, int end) {
		String result = null;
		try {
			result = jedisCluster.substr(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long hset(String key, String field, String value) {
		Long result = null;
		try {
			result = jedisCluster.hset(key, field, value);
			//同时更新本地缓存
			local_hset(key, field, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String hget(String key, String field) {
		String result = null;
		try {
			result = jedisCluster.hget(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		local_hset(key, field, result);
		return result;
	}

	public Long hsetnx(String key, String field, String value) {
		Long result = null;
		try {
			result = jedisCluster.hsetnx(key, field, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String hmset(String key, Map<String, String> hash) {
		String result = null;
		try {
			result = jedisCluster.hmset(key, hash);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public List<String> hmget(String key, String... fields) {
		List<String> result = null;
		try {
			result = jedisCluster.hmget(key, fields);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long hincrBy(String key, String field, long value) {
		Long result = null;
		try {
			result = jedisCluster.hincrBy(key, field, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Boolean hexists(String key, String field) {
		Boolean result = false;
		try {
			result = jedisCluster.hexists(key, field);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long del(String key) {
		Long result = null;
		try {
			result = jedisCluster.del(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long hdel(String key, String field) {
		Long result = null;
		try {
			result = jedisCluster.hdel(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long hlen(String key) {
		Long result = null;
		try {
			result = jedisCluster.hlen(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> hkeys(String key) {
		Set<String> result = null;
		try {
			result = jedisCluster.hkeys(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public List<String> hvals(String key) {
		List<String> result = null;
		try {
			result = jedisCluster.hvals(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;
		try {
			result = jedisCluster.hgetAll(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		//更新本地缓存
		local_hsetAll(key, result);
		return result;
	}


	// ================list ====== l表示 list,l表示left, r表示right====================
	public Long rpush(String key, String string) {
		Long result = null;
		try {
			result = jedisCluster.rpush(key, string);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long lpush(String key, String string) {
		Long result = null;
		try {
			result = jedisCluster.lpush(key, string);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long llen(String key) {
		Long result = null;
		try {
			result = jedisCluster.llen(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public List<String> lrange(String key, long start, long end) {
		List<String> result = null;
		try {
			result = jedisCluster.lrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String ltrim(String key, long start, long end) {
		String result = null;
		try {
			result = jedisCluster.ltrim(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String lindex(String key, long index) {
		String result = null;
		try {
			result = jedisCluster.lindex(key, index);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String lset(String key, long index, String value) {
		String result = null;
		try {
			result = jedisCluster.lset(key, index, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long lrem(String key, long count, String value) {
		Long result = null;
		try {
			result = jedisCluster.lrem(key, count, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String lpop(String key) {
		String result = null;
		try {
			result = jedisCluster.lpop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String rpop(String key) {
		String result = null;
		try {
			result = jedisCluster.rpop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	// return 1 add a not exist value ,
	// return 0 add a exist value
	public Long sadd(String key, String member) {
		Long result = null;
		try {
			result = jedisCluster.sadd(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}
	
	public Long sadd(String key, String... member) {
		Long result = null;
		try {
			result = jedisCluster.sadd(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> smembers(String key) {
		Set<String> result = null;
		try {
			result = jedisCluster.smembers(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long srem(String key, String member) {
		Long result = null;
		try {
			result = jedisCluster.srem(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String spop(String key) {
		String result = null;
		try {
			result = jedisCluster.spop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long scard(String key) {
		Long result = null;
		try {
			result = jedisCluster.scard(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Boolean sismember(String key, String member) {
		Boolean result = null;
		try {
			result = jedisCluster.sismember(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String srandmember(String key) {
		String result = null;
		try {
			result = jedisCluster.srandmember(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zadd(String key, double score, String member) {
		Long result = null;
		try {
			result = jedisCluster.zadd(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;
		try {
			result = jedisCluster.zrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zrem(String key, String member) {
		Long result = null;
		try {
			result = jedisCluster.zrem(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Double zincrby(String key, double score, String member) {
		Double result = null;
		try {
			result = jedisCluster.zincrby(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zrank(String key, String member) {
		Long result = null;
		try {
			result = jedisCluster.zrank(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zrevrank(String key, String member) {
		Long result = null;
		try {
			result = jedisCluster.zrevrank(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> zrevrange(String key, int start, int end) {
		Set<String> result = null;
		try {
			result = jedisCluster.zrevrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		try {
			result = jedisCluster.zrangeWithScores(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		try {
			result = jedisCluster.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zcard(String key) {
		Long result = null;
		try {
			result = jedisCluster.zcard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Double zscore(String key, String member) {
		Double result = null;
		try {
			result = jedisCluster.zscore(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public List<String> sort(String key) {
		List<String> result = null;
		try {
			result = jedisCluster.sort(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;
		try {
			result = jedisCluster.sort(key, sortingParameters);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zcount(String key, double min, double max) {
		Long result = null;
		try {
			result = jedisCluster.zcount(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> result = null;
		try {
			result = jedisCluster.zrangeByScore(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> result = null;
		try {
			result = jedisCluster.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<String> result = null;
		try {
			result = jedisCluster.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> result = null;
		try {
			result = jedisCluster.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> result = null;
		try {
			result = jedisCluster.zrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> result = null;
		try {
			result = jedisCluster.zrevrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		try {
			result = jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		try {
			result = jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zremrangeByRank(String key, int start, int end) {
		Long result = null;
		try {
			result = jedisCluster.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long zremrangeByScore(String key, double start, double end) {
		Long result = null;
		try {
			result = jedisCluster.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Long result = null;
		try {
			result = jedisCluster.linsert(key, where, pivot, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 存储数据到缓存中，并制定过期时间和当Key存在时是否覆盖
	 * @param key
	 * @param value
	 * @param nxxx 只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
	 * @param expx 只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒
	 * @param expireTime 过期时间
	 */
	public String set(String key, String value, String nxxx,String expx,int expireTime) {
		String result = null;
		try {
			result = jedisCluster.set(key, value, nxxx, expx, expireTime);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	
	/**
	 * 执行redis脚本
	 * @param secKillScript 脚本代码
	 * @param keyCount key个数
	 * @return
	 */
	public String evalScript(String secKillScript, int keyCount,String... params) {
		Object result = jedisCluster.eval(secKillScript, keyCount,params);
		return String.valueOf(result);
	}

	public Double incrByFloat(String key, double integer) {
		Double result = null;
		try {
			result = jedisCluster.incrByFloat(key, integer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}


	public void disconnect() {
		jedisCluster.quit();
	}

}
