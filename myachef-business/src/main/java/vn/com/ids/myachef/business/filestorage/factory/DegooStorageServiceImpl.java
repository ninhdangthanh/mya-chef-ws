package vn.com.ids.myachef.business.filestorage.factory;

import org.springframework.stereotype.Component;

@Component
public class DegooStorageServiceImpl implements StorageService {

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.DEGOO;
    }

}
