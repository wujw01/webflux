package com.runrunfast.webflux.service;

import com.runrunfast.webflux.bean.City;
import com.runrunfast.webflux.dao.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @version <p>V1.0</p>
 * @Author: <H2>james</H2>
 * @Description: <P>webflux处理器</P>
 * @Date: <P>CREATE IS 2018/9/13 11:55</P>
 * 从返回值可以看出，Mono 和 Flux 适用于两个场景，即：
 * Mono：实现发布者，并返回 0 或 1 个元素，即单对象。
 * Flux：实现发布者，并返回 N 个元素，即 List 列表对象。
 **/
@Component
public class CityHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityHandler.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private final CityRepository cityRepository;

    @Autowired
    public CityHandler(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public long save(City city) {
        return cityRepository.save(city);
    }


    public Mono<City> findCityById(Long id) {
        // 从缓存中获取城市信息
        String key = "city_" + id;
        ValueOperations<String, City> operations = redisTemplate.opsForValue();
        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            City city = operations.get(key);
            LOGGER.info("CityHandler.findCityById() : 从缓存中获取了城市 >> " + city.toString());
            return Mono.create(cityMonoSink -> cityMonoSink.success(city));
        }
        // 从 MongoDB 中获取城市信息
        Mono<City> cityMono = Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.findCityById(id)));
        if (cityMono == null)
            return cityMono;
        // 插入缓存
        cityMono.subscribe(cityObj -> {
            operations.set(key, cityObj);
            LOGGER.info("CityHandler.findCityById() : 城市插入缓存 >> " + cityObj.toString());
        });
        return cityMono;
    }

    public Flux<City> findAllCity() {
        return Flux.fromIterable(cityRepository.findAll());
    }

    public Mono<Long> modifyCity(City city) {
        Mono<Long> cityMono = Mono.create(cityMonoSink  -> cityMonoSink.success(cityRepository.save(city)));
        // 缓存存在，删除缓存
        String key = "city_" + city.getId();
        System.out.println(key);
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);

            LOGGER.info("CityHandler.modifyCity() : 从缓存中删除城市 ID >> " + city.getId());
        }
        return cityMono;
    }

    public Mono<Long> deleteCity(Long id) {
        cityRepository.deleteCity(id);
        // 缓存存在，删除缓存
        String key = "city_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);

            LOGGER.info("CityHandler.deleteCity() : 从缓存中删除城市 ID >> " + id);
        }
        return Mono.create(cityMonoSink -> cityMonoSink.success(id));
    }
}
