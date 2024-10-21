package com.runrunfast.webflux.dao;

import com.runrunfast.webflux.bean.City;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version <p>V1.0</p>
 * @Author: <H2>james</H2>
 * @Description: <P>添加说明</P>
 * @Date: <P>CREATE IS 2018/9/13 13:45</P>
 **/
@Repository
public class CityRepository {

    private ConcurrentMap<Long, City> repository = new ConcurrentHashMap<>();

    private static final AtomicLong idGenerator = new AtomicLong(0);

    /**
     * 新增
     * @param city
     * @return
     */
    public Long save(City city) {
        Long id = idGenerator.incrementAndGet();
        city.setId(id);
        repository.put(id, city);
        return id;
    }

    /**
     * 查找
     * @return
     */
    public Collection<City> findAll() {
        return repository.values();
    }

    /**
     * 单个查询
     * @param id
     * @return
     */
    public City findCityById(Long id) {
        return repository.get(id);
    }

    /**
     * 修改
     * @param city
     * @return
     */
    public Long updateCity(City city) {
        repository.put(city.getId(), city);
        return city.getId();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Long deleteCity(Long id) {
        repository.remove(id);
        return id;
    }
}
