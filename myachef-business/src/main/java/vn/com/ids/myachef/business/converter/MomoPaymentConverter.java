package vn.com.ids.myachef.business.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.ids.myachef.business.payload.reponse.momo.MomoCreatePaymentResponse;
import vn.com.ids.myachef.dao.model.MomoPaymentModel;

@Component
public class MomoPaymentConverter {

    @Autowired
    private ModelMapper mapper;

    public MomoPaymentModel toMomoPaymentModel(MomoCreatePaymentResponse response) {
        return mapper.map(response, MomoPaymentModel.class);
    }
}
