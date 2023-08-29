package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.MomoPaymentModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface MomoPaymentRepository extends GenericRepository<MomoPaymentModel, Long>, JpaSpecificationExecutor<MomoPaymentModel> {

    public MomoPaymentModel findByRequestId(String requestId);

}