package vn.com.ids.myachef.business.service;

import java.io.Serializable;
import java.util.List;

public interface IGenericService<D extends Serializable, K extends Object> {

    D findOne(final K id);

    List<D> findAll();

    D save(final D entity);

    D saveAndFlush(final D entity);

    void delete(final D entity);

    void deleteById(final K entityId);

    void deleteByIds(List<K> ids);

    List<D> saveAll(List<D> entity);

    List<D> findAllById(List<K> entityIds);

}