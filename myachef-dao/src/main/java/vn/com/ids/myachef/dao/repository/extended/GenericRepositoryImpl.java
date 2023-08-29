package vn.com.ids.myachef.dao.repository.extended;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional
public class GenericRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericRepository<T, ID> {

    private static final String OBJECT_ID = "id";
    private final EntityManager entityManager;
    private final JpaEntityInformation<T, ID> entityInformation;

    public GenericRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    private List<T> fetchEntityGraph(CriteriaQuery<T> query, Root<T> root, List<Order> orders, List<T> objects, String entityGraphName) {
        Predicate predicate = root.in(objects);
        EntityGraph<T> entityGraph = entityManager.createEntityGraph(this.getDomainClass());
        entityGraph.addSubgraph(entityGraphName);

        objects = entityManager.createQuery( //
                query.select(root) //
                        .where(predicate) //
                        .orderBy(orders)) //
                .setHint("javax.persistence.fetchgraph", entityGraph) //
                .getResultList();
        return objects;
    }

    @Override
    public List<T> findAllEntityGraph(List<Long> ids, Sort sort, List<String> entityGraphNames) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(this.getDomainClass());
        Root<T> root = query.from(this.getDomainClass());
        List<Order> orders = sort != null ? toOrders(sort, root, builder) : new ArrayList<>();

        Predicate predicate = root.get(OBJECT_ID).in(ids);

        // find entity by ids only
        if (CollectionUtils.isEmpty(entityGraphNames)) {
            return entityManager.createQuery( //
                    query.select(root) //
                            .where(predicate) //
                            .orderBy(orders)) //
                    .getResultList();
        }

        // find entity graph by ids
        String entityGraphName = entityGraphNames.get(0);
        EntityGraph<T> entityGraph = entityManager.createEntityGraph(this.getDomainClass());
        entityGraph.addSubgraph(entityGraphName);

        List<T> objects = entityManager.createQuery( //
                query.select(root) //
                        .where(predicate) //
                        .orderBy(orders)) //
                .setHint("javax.persistence.fetchgraph", entityGraph) //
                .getResultList();

        // fetch another graph
        if (entityGraphNames.size() > 1) {
            // for (String entityGraphName : entityGraphNames) {
            for (int i = 1; i < entityGraphNames.size(); i++) {
                objects = fetchEntityGraph(query, root, orders, objects, entityGraphNames.get(i));
            }
        }

        return objects;
    }

    @Override
    public Page<ID> findIdsByCriteria(Specification<T> specification, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();

        // count by criteria
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(this.getDomainClass());
        Predicate countPredicate = specification.toPredicate(countRoot, countQuery, criteriaBuilder);
        countQuery = countQuery.select(criteriaBuilder.count(countRoot)).where(countPredicate);
        TypedQuery<Long> typedCountQuery = entityManager.createQuery(countQuery);

        // get list id by criteria
        CriteriaQuery<ID> idsQuery = criteriaBuilder.createQuery(this.entityInformation.getIdType());
        Root<T> idsRoot = idsQuery.from(this.getDomainClass());
        Predicate idsPredicate = specification.toPredicate(idsRoot, idsQuery, criteriaBuilder);

        // update Sorting
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        if (sort.isSorted()) {
            idsQuery.orderBy(toOrders(sort, idsRoot, criteriaBuilder));
        }

        idsQuery = idsQuery.multiselect((Path<Object>) idsRoot.get(OBJECT_ID)).where(idsPredicate);
        TypedQuery<ID> typedIdsQuery = entityManager.createQuery(idsQuery);

        // update Pagination attributes
        if (pageable.isPaged()) {
            typedIdsQuery.setFirstResult((int) pageable.getOffset());
            typedIdsQuery.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(typedIdsQuery.getResultList(), pageable, typedCountQuery::getSingleResult);
    }
}