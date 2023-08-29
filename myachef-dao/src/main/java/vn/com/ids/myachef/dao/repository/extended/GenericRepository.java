package vn.com.ids.myachef.dao.repository.extended;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    public Page<ID> findIdsByCriteria(Specification<T> specification, Pageable pageable);

    public List<T> findAllEntityGraph(List<Long> ids, Sort sort, List<String> entityGraphNames);
}