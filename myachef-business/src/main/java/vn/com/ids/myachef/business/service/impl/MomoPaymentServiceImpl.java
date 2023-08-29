package vn.com.ids.myachef.business.service.impl;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.payload.reponse.momo.MomoPaymentRedirectBody;
import vn.com.ids.myachef.business.service.MomoPaymentService;
import vn.com.ids.myachef.dao.enums.MomoPaymentStatus;
import vn.com.ids.myachef.dao.model.MomoPaymentModel;
import vn.com.ids.myachef.dao.repository.MomoPaymentRepository;

@Service
@Transactional
@Slf4j
public class MomoPaymentServiceImpl extends AbstractService<MomoPaymentModel, Long> implements MomoPaymentService {

    private MomoPaymentRepository momoPaymentRepository;

    protected MomoPaymentServiceImpl(MomoPaymentRepository momoPaymentRepository) {
        super(momoPaymentRepository);
        this.momoPaymentRepository = momoPaymentRepository;
    }

    @Override
    public MomoPaymentModel findByRequestId(String requestId) {
        return momoPaymentRepository.findByRequestId(requestId);
    }

    @Override
    public void handleRedirect(MomoPaymentRedirectBody body) {
        MomoPaymentModel momoPaymentModel = momoPaymentRepository.findByRequestId(body.getRequestId());
        if (momoPaymentModel == null) {
            log.error("Not found momo payment with requestId: " + body.getRequestId());
        } else {
            momoPaymentModel.setMessage(body.getMessage());
            momoPaymentModel.setResultCode(body.getResultCode());
            momoPaymentModel.setTransId(body.getTransId());
            if (body.getResultCode() == 0) {
                momoPaymentModel.setStatus(MomoPaymentStatus.SUCCESS);
                momoPaymentModel.setSuccessDate(LocalDateTime.now());

                log.error("Momo payment success");
            } else {
                momoPaymentModel.setStatus(MomoPaymentStatus.FAILED);

                log.error("Momo payment failed message {}", body.getMessage());
            }

            momoPaymentRepository.save(momoPaymentModel);
        }

        log.info("------------------ Momo payment redirect url - END ----------------");
    }
}
