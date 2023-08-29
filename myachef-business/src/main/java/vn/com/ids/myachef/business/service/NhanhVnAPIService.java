package vn.com.ids.myachef.business.service;

import org.springframework.http.ResponseEntity;

import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductCategoryResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductDetailResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductResponse;

public interface NhanhVnAPIService {

    public void getAccessToken(String accessCode);

    public ResponseEntity<?> receiveWebHooks(String data);

    public NhanhVnProductCategoryResponse getListCategory();

    public NhanhVnProductDetailResponse getById(String productId);

    NhanhVnProductResponse getListProduct(String pagination);
}
