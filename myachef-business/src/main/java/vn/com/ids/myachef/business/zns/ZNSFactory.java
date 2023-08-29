package vn.com.ids.myachef.business.zns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.ids.myachef.dao.enums.ZaloZNSChannel;

@Component
public class ZNSFactory {

    private static final Map<ZaloZNSChannel, ZNSService> channel = new HashMap<>();

    @Autowired
    private ZNSFactory(List<ZNSService> viewers) {
        for (ZNSService viewer : viewers) {
            channel.put(viewer.getType(), viewer);
        }
    }

    public ZNSService getFactory(ZaloZNSChannel type) {
        ZNSService znsService = channel.get(type);
        if (znsService == null) {
            throw new UnsupportedOperationException("This type is unsupported ");
        }
        return znsService;
    }

}
