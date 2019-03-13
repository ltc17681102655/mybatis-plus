package com.ltc.redis.utils;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

/**
 * Redis工具类
 *
 * @author Merchant cloud platform development team.
 */
@Component
@Slf4j
public class RedisUtils {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, String> hashOperations;
    @Autowired
    private ListOperations<String, String> listOperations;
    @Autowired
    private SetOperations<String, String> setOperations;
    @Autowired
    private ZSetOperations<String, String> zSetOperations;
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;

    public <T> T get(String key, Class<T> clazz, long expire) {
        Object value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(Object json, Class<T> clazz) {
        return JSON.parseObject(json.toString(), clazz);
    }

    //=============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Set<String> keys(String pattern) {
        try {
            if (pattern != null && !"".equals(pattern)) {
                return this.redisTemplate.keys(pattern);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<String>();
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expireAt(String key, Date date) {
        try {
            redisTemplate.expireAt(key, date);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String key) {
        if (key != null && !"".equals(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(Collection<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    //============================String=============================  

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : this.valueOperations.get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            this.valueOperations.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                this.valueOperations.set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param by  要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key 键
     * @param by  要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================  

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public String hget(String key, String item) {
        return hashOperations.get(key, item);
    }

    public <R> R hget(String key, String item, Class<R> clazz, Supplier<R> supplier) {
        try {
            String string = hashOperations.get(key, item);
            if (!StringUtils.isEmpty(string)) {
                return fromJson(string, clazz);
            }
            throw new RuntimeException("redis value is empty");
        } catch (Exception e) {
            R value = supplierByCounter(key + item, supplier, "[hget] " + e.getMessage());
            if (value != null) {
                hset(key, item, toJson(value));
            }
            return value;
        }
    }

    public void multiSet(Map<String, String> keyValueMap) {
        valueOperations.multiSet(keyValueMap);
    }

    public List<String> multiGet(Collection<String> keys) {
        return valueOperations.multiGet(keys);
    }

    public <T> List<T> multiGet(Collection<String> keys, Class<T> clazz) {
        List<String> list = multiGet(keys);

        List<T> resultList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(list)) {
            for (String i : list) {
                if (!StringUtils.isEmpty(i)) {
                    resultList.add(JSON.parseObject(i, clazz));
                }
            }
        }
        return resultList;
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, String> hmget(String key) {
        return hashOperations.entries(key);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public List<String> hmget(String key, Collection<String> hashKeys) {
        return hashOperations.multiGet(key, hashKeys);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public void putAll(String key, Map<String, String> fieldValueMap) {
        hashOperations.putAll(key, fieldValueMap);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, String> map) {
        try {
            hashOperations.putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, String value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("[hset] redis write failed", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 获取Hash中所有的field
     *
     * @param key
     * @return
     */
    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    //============================set=============================  

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<String> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, String value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, String... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, String... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, String... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================  

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<String> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public String lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, String value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<String> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<String> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, String value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Redis Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。
     * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param string
     * @param i
     * @param string2
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    public boolean zadd(String key, double score, String member) {
        try {
            return this.zSetOperations.add(key, member, score);
        } catch (Exception e) {
            log.error("redis zadd failed", e);
            return false;
        }
    }

    public Long zremove(String key, String... values) {
        return this.zSetOperations.remove(key, values);
    }

    public Set<String> zrange(String key, long start, long end) {
        return this.zSetOperations.range(key, start, end);
    }

    public Set<String> reverseRange(String key, long start, long end) {
        return this.zSetOperations.reverseRange(key, start, end);
    }

    public Long zSetSize(String key) {
        return zSetOperations.size(key);
    }

    /**
     * @param key      zset key
     * @param start    start index
     * @param end      end index
     * @param supplier callback
     * @param <R>      List<Map<String, Object>>> map key: score, value: zset item
     * @return
     */
    public <R extends List<Map<String, Object>>> List<String> reverseRange(String key, long start, long end, Supplier<R> supplier) {
        try {
            Set<String> set = zSetOperations.reverseRange(key, start, end);
            if (!CollectionUtils.isEmpty(set)) {
                return Lists.newArrayList(set);
            }
            throw new RuntimeException("redis value is empty");
        } catch (Exception e) {
            R list = supplierByCounter(key, supplier, "[reverseRange] " + e.getMessage());
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(i -> zadd(key, Double.parseDouble(i.get("score").toString()), i.get("value").toString()));
                return list.stream().map(i -> i.get("value").toString()).collect(toList());
            }
            return Collections.emptyList();
        }
    }

    public <T> List<T> batchHGet(String key, List<String> fields, Class<T> clazz) {
        return batchHGet(key, fields, clazz, null);
    }

    public <V, T extends List<String>, R extends List<V>> List<V> batchHGet(String key, List<String> fields, Class<V> clazz, Function<T, R> function) {
        List<V> resultList = Lists.newArrayList();
        List<Object> list = null;

        try {
            list = redisTemplate.executePipelined(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisConnection redisConnection = (StringRedisConnection) connection;
                    for (String field : fields) {
                        redisConnection.hGet(key, field);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            log.warn("[batchHGet] redis operations failed, key:" + key + ", fields:" + JSON.toJSONString(fields), e);
        }

        List<String> missingList = Lists.newArrayList();
        List<String> resultIdList = Lists.newArrayList();

        if (CollectionUtils.isEmpty(list)) {
            missingList = fields;
        } else {
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);
                if (!StringUtils.isEmpty(obj)) {
                    V data = JSON.parseObject(obj.toString(), clazz);
                    resultIdList.add(BeanCopyUtils.getValue(data, "id").toString());
                    resultList.add(data);
                }
            }
            missingList = fields.stream().filter(t -> !resultIdList.contains(t)).collect(toList());
        }

        if (!CollectionUtils.isEmpty(missingList) && function != null) {
            T idList = (T) missingList;
            R dbList = functionByCounter(key, function, idList, "missing ids");
            if (!CollectionUtils.isEmpty(dbList)) {
                resultList.addAll(dbList);

                for (int i = 0; i < dbList.size(); i++) {
                    V data = dbList.get(i);
                    hset(key, BeanCopyUtils.getValue(data, "id").toString(), JSON.toJSONString(data));
                }
            }
        }

        return resultList;
    }

    /**
     * 当Supplier操作连续两次返回null，则不会再继续执行Supplier操作，直到计数器大于10，会重置计数器
     * <p>
     * Ex: 每10次查询，至多执行两次数据库查询，若数据库查到值，会回写到redis，则不会再调用supplier。
     *
     * @param key
     * @param supplier
     * @param message
     * @param <R>
     * @return
     */
    private static <R> R supplierByCounter(String key, Supplier<R> supplier, String message) {
        R value = null;
        if (CacheHitCounter.getHitCount(key) < 2) {
            log.warn(message + ", redis operations failed, exec db operations, key: " + key);
            value = supplier.get();

            if (value == null || (value instanceof List && ((List) value).size() == 0)) {
                CacheHitCounter.incrementAndGet(key);
            } else {
                CacheHitCounter.reset(key);
            }
        } else {
            CacheHitCounter.incrementAndGet(key);
        }

        if (CacheHitCounter.getHitCount(key) > 10) {
            CacheHitCounter.reset(key);
        }
        return value;
    }

    private static <T, R> R functionByCounter(String key, Function<T, R> function, T param, String message) {
        R value = null;
        if (CacheHitCounter.getHitCount(key) < 2) {
            log.warn(message + ", redis operations failed, exec db operations, key: " + key + " params: " + JSON.toJSONString(param));
            value = function.apply(param);

            if (value == null || (value instanceof List && ((List) value).size() == 0)) {
                CacheHitCounter.incrementAndGet(key);
            } else {
                CacheHitCounter.reset(key);
            }
        } else {
            CacheHitCounter.incrementAndGet(key);
        }

        if (CacheHitCounter.getHitCount(key) > 10) {
            CacheHitCounter.reset(key);
        }
        return value;
    }

}
