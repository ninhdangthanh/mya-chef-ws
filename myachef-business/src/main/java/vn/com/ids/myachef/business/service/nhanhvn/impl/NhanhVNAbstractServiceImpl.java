package vn.com.ids.myachef.business.service.nhanhvn.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vn.com.ids.myachef.business.service.NhanhVnService;

@Service
@Transactional
public class NhanhVNAbstractServiceImpl {

    protected static final String VERSION = "version";
    protected static final String APP_ID = "appId";
    protected static final String BUSINESS_ID = "businessId";
    protected static final String ACCESS_TOKEN = "accessToken";
    protected static final String DATA = "data";

    @Autowired
    protected NhanhVnService nhanhVnService;

    private Gson initialize() {
        return new GsonBuilder() //
                .serializeNulls() //
                .disableHtmlEscaping() //
                .setPrettyPrinting() //
                .create();
    }

    protected String toJson(Object object) {
        return initialize()//
                .toJson(object);
    }
}
