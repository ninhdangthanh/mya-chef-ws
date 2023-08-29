package vn.com.ids.myachef.business.filestorage.factory;

import org.springframework.stereotype.Component;

@Component
public class IDSStorageServiceImpl implements StorageService {

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.IDS;
    }

}
