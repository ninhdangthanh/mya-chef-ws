package vn.com.ids.myachef.business.zns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class ZNSAbstractFactory {

    public Gson initialize() {
        return new GsonBuilder() //
                .serializeNulls() //
                .disableHtmlEscaping() //
                .setPrettyPrinting() //
                .create();
    }
}