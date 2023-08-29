package vn.com.ids.myachef.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.service.IGenericService;
import vn.com.ids.myachef.dao.criteria.AbstractCriteria;

@Transactional
public abstract class AbstractService<D extends Serializable, K extends Object> implements IGenericService<D, K> {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final JpaRepository<D, K> jpaRepository;

    protected AbstractService(JpaRepository<D, K> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    protected JpaRepository<D, K> repository() {
        return jpaRepository;
    }

    @Override
    public D findOne(K id) {
        Optional<D> item = jpaRepository.findById(id);
        return item.isPresent() ? item.get() : null;
    }

    @Override
    public List<D> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public D save(D dto) {
        return jpaRepository.save(dto);
    }

    @Override
    public List<D> findAllById(List<K> entityIds) {
        return jpaRepository.findAllById(entityIds);
    }

    @Override
    public List<D> saveAll(List<D> dtos) {
        List<D> result = new ArrayList<>();
        List<D> temp = new ArrayList<>();
        int counter = 0;
        for (D d : dtos) {
            temp.add(d);
            if ((counter + 1) % batchSize == 0 || (counter + 1) == dtos.size()) {
                result.addAll(jpaRepository.saveAll(temp));
                temp.clear();
            }
            counter++;
        }

        return result;
    }

    @Override
    public D saveAndFlush(D dto) {
        return jpaRepository.saveAndFlush(dto);
    }

    @Override
    public void delete(D dto) {
        jpaRepository.delete(dto);
    }

    @Override
    public void deleteById(K id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<K> ids) {
        jpaRepository.deleteAllById(ids);
    }

    protected Order buildOrder(AbstractCriteria abstractCriteria) {
        if (StringUtils.hasText(abstractCriteria.getSortBy())) {
            if (Boolean.TRUE.equals(abstractCriteria.getAscending())) {
                return new Order(Sort.Direction.ASC, abstractCriteria.getSortBy());
            } else {
                return new Order(Sort.Direction.DESC, abstractCriteria.getSortBy());
            }
        }
        return null;
    }

    public Pageable buildPageable(AbstractCriteria abstractCriteria) {
        Order order = buildOrder(abstractCriteria);
        if (order != null) {
            return PageRequest.of(abstractCriteria.getPageIndex(), abstractCriteria.getPageSize(), Sort.by(order));
        }
        return PageRequest.of(abstractCriteria.getPageIndex(), abstractCriteria.getPageSize());
    }

    public Pageable buildPageable(int pageIndex, int pageSize) {
        return PageRequest.of(pageIndex, pageSize);
    }

}
