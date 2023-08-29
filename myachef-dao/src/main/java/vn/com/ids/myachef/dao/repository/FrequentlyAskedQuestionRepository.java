package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.FrequentlyAskedQuestionModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface FrequentlyAskedQuestionRepository extends GenericRepository<FrequentlyAskedQuestionModel, Long>, JpaSpecificationExecutor<FrequentlyAskedQuestionModel> {    

}
