package com.project.wdx.distributedsession.repository.redis;

import com.project.wdx.distributedsession.repository.DataRepository;
import com.project.wdx.distributedsession.utils.SerializerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDataRepository implements DataRepository {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public <T>void save(String sessionId, T obj) {
        ValueOperations redisOperater =  redisTemplate.opsForValue();
        redisOperater.set(sessionId, SerializerUtils.serialize(obj));

    }

    @Override
    public String  queryBySessionId(String seesionId) {
        Object obj = redisTemplate.opsForValue().get(seesionId);
        return obj.toString();
    }

    @Override
    public <T> T queryBySessionId(String sessionId, Class<T> clazz) {
        Object obj = redisTemplate.opsForValue().get(sessionId);

        return SerializerUtils.deSerialize(obj.toString(),clazz);
    }

}
