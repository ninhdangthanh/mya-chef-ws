package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.enums.BannerType;
import vn.com.ids.myachef.dao.model.FileUploadModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface FileUploadRepository extends GenericRepository<FileUploadModel, Long>, JpaSpecificationExecutor<FileUploadModel> {

    FileUploadModel findByBannerTypeAndIsBannerTrue(BannerType type);

}
