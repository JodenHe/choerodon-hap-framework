package io.choerodon.hap;

import io.choerodon.hap.account.dto.Role;
import io.choerodon.hap.cache.impl.UserCache;
import io.choerodon.hap.function.dto.Function;
import io.choerodon.hap.function.dto.Resource;
import io.choerodon.hap.system.dto.Form;
import io.choerodon.hap.system.dto.SysConfig;
import io.choerodon.hap.system.dto.SysPreferences;
import io.choerodon.redis.impl.CacheReloadProcessor;
import io.choerodon.redis.impl.HashStringRedisCache;
import io.choerodon.redis.impl.HashStringRedisCacheGroup;
import io.choerodon.redis.impl.RedisCache;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

    @SuppressWarnings("unchecked")
    @Bean(value = "functionCache")
    public HashStringRedisCacheGroup functionCache() {
        HashStringRedisCacheGroup group = new HashStringRedisCacheGroup();
        group.setName("function");
        group.setType(Function.class);
        group.setGroupField(StringUtils.split("lang"));
        group.setKeyField(StringUtils.split("functionId"));
        group.setLoadOnStartUp(true);
        group.setSqlId("io.choerodon.hap.function.mapper.FunctionMapper.selectForCache");
        return group;
    }

    @SuppressWarnings("unchecked")
    @Bean(value = "preferenceCache")
    public HashStringRedisCacheGroup preferenceCache() {
        HashStringRedisCacheGroup group = new HashStringRedisCacheGroup();
        group.setName("preference");
        group.setType(SysPreferences.class);
        group.setGroupField(StringUtils.split("userId"));
        group.setKeyField(StringUtils.split("preferences"));
        group.setLoadOnStartUp(true);
        group.setSqlId("io.choerodon.hap.system.mapper.SysPreferencesMapper.selectAll");
        return group;
    }

    @SuppressWarnings("unchecked")
    @Bean(value = "formCache")
    public HashStringRedisCache formCache() {
        HashStringRedisCache group = new HashStringRedisCache();
        group.setName("form");
        group.setType(Form.class);
        group.setKeyField(StringUtils.split("code"));
        group.setLoadOnStartUp(true);
        group.setSqlId("io.choerodon.hap.system.mapper.FormBuilderMapper.selectAll");
        return group;
    }

    @SuppressWarnings("unchecked")
    @Bean(value = "configCache")
    public HashStringRedisCache configCache() {
        HashStringRedisCache group = new HashStringRedisCache();
        group.setName("config");
        group.setType(SysConfig.class);
        group.setKeyField(StringUtils.split("configCode"));
        group.setLoadOnStartUp(true);
        group.setSqlId("io.choerodon.hap.system.mapper.SysConfigMapper.selectAll");
        return group;
    }

    @Bean(value = "cacheReloadProcessor")
    public CacheReloadProcessor cacheReloadProcessor() {
        CacheReloadProcessor cacheReloadProcessor = new CacheReloadProcessor();
        cacheReloadProcessor.setPublishMessageTo(StringUtils.split("topic:cache:reloaded"));
        cacheReloadProcessor.setQueue("queue:cache:reload");
        return cacheReloadProcessor;
    }


    @SuppressWarnings("unchecked")
    @Bean(value = "resourceUrlCache")
    public RedisCache resourceUrlCache() {
        RedisCache redisCache = new RedisCache();
        redisCache.setName("resource_url");
        redisCache.setType(Resource.class);
        redisCache.setKeyField(StringUtils.split("url"));
        redisCache.setLoadOnStartUp(true);
        redisCache.setSqlId("io.choerodon.hap.function.mapper.ResourceMapper.selectAll");
        return redisCache;
    }

    @SuppressWarnings("unchecked")
    @Bean(value = "resourceIdCache")
    public RedisCache resourceIdCache() {
        RedisCache redisCache = new RedisCache();
        redisCache.setName("resource_id");
        redisCache.setType(Resource.class);
        redisCache.setKeyField(StringUtils.split("resourceId"));
        redisCache.setLoadOnStartUp(true);
        redisCache.setSqlId("io.choerodon.hap.function.mapper.ResourceMapper.selectAll");
        return redisCache;
    }

    @SuppressWarnings("unchecked")
    @Bean(value = "roleCache")
    public RedisCache roleCache() {
        RedisCache redisCache = new RedisCache();
        redisCache.setName("role");
        redisCache.setType(Role.class);
        redisCache.setKeyField(StringUtils.split("roleCode"));
        redisCache.setLoadOnStartUp(true);
        redisCache.setSqlId("io.choerodon.hap.account.mapper.RoleMapper.selectAll");
        return redisCache;
    }


    @Bean(value = "userCache")
    public UserCache userCache() {
        UserCache userCache = new UserCache();
        userCache.setName("user");
        return userCache;
    }

}
