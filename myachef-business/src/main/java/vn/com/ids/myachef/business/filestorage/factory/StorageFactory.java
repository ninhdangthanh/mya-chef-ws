package vn.com.ids.myachef.business.filestorage.factory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageFactory {

    private static final Map<StorageChannel, StorageService> storageChannelMap = new EnumMap<>(StorageChannel.class);

    @Autowired
    private StorageFactory(List<StorageService> viewers) {
        for (StorageService viewer : viewers) {
            storageChannelMap.put(viewer.getChannel(), viewer);
        }
    }

    public StorageService getFactory(StorageChannel deviceType) {
        StorageService tokenService = storageChannelMap.get(deviceType);
        if (tokenService == null) {
            throw new UnsupportedOperationException("This Storage channel is unsupported ");
        }
        return tokenService;
    }

}
