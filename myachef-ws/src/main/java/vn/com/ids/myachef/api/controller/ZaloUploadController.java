package vn.com.ids.myachef.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.reponse.ZaloFileUploadResponse;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.service.SubscriptionCustomerDetailService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;

@RestController
public class ZaloUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SubscriptionCustomerDetailService subscriptionCustomerDetailService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/upload/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ZaloFileUploadResponse uploadFile(@RequestParam("file") List<MultipartFile> files, //
            @RequestParam(value = "id", required = true) Long id, //
            @RequestParam(value = "type", required = true) UploadType type, //
            HttpServletRequest request, HttpServletResponse response) {

        List<String> urls = new ArrayList<>();

        MultipartFile file = files.get(0);
        if (file == null) {
            throw new BadRequestException("File can be not blank");
        }

        String generatedName = null;
        String imageLink = null;
        switch (type) {
        case CUSTOMER:
            CustomerModel customerModel = customerService.findOne(id);
            if (customerModel == null) {
                throw new ResourceNotFoundException("Not found customer by id: " + id);
            }
            generatedName = generatedFilename(file, "avatar_");

            fileStorageService.upload(String.format(applicationConfig.getFullCustomerAvatarPath(), id), generatedName, file);

            imageLink = fileUploadService.getFilePath(String.format("%s%s/", applicationConfig.getCustomerAvatarPath(), id), generatedName, request);
            urls.add(imageLink);
            
            customerModel.setAvatar(imageLink);
            customerService.save(customerModel);
            
            break;

        case ORDER:
            OrderModel orderModel = orderService.findOne(id);
            if (orderModel == null) {
                throw new ResourceNotFoundException(String.format("Not found order by id: %s", id));
            }

            generatedName = generatedFilename(file, "order_");
            fileStorageService.upload(String.format(applicationConfig.getFullOrderPath(), id), generatedName, file);
            imageLink = fileUploadService.getFilePath(String.format("%s%s/", applicationConfig.getOrderPath(), id), generatedName, request);

            urls.add(imageLink);

            orderModel.setPaymentImage(imageLink);
            orderService.save(orderModel);
            
            break;

        case SUBSCRIPTION:
            SubscriptionCustomerDetailModel subscriptionCustomerDetailModel = subscriptionCustomerDetailService.findOne(id);
            if (subscriptionCustomerDetailModel == null) {
                throw new ResourceNotFoundException("Not found SubscriptionCustomerDetail by id: " + id);
            }
            generatedName = generatedFilename(file, "subscription_customer_");
            fileStorageService.upload(String.format(applicationConfig.getFullSubscriptionBillPath(), id), generatedName, file);

            imageLink = fileUploadService.getFilePath(String.format("%s%s/", applicationConfig.getSubscriptionBillPath(), id), generatedName, request);

            urls.add(imageLink);
            
            subscriptionCustomerDetailModel.setBillImage(imageLink);
            subscriptionCustomerDetailService.save(subscriptionCustomerDetailModel);

            break;

        default:
            break;
        }

        return new ZaloFileUploadResponse(0, "Success", urls, generatedName);
    }

    private String generatedFilename(MultipartFile file, String prefixName) {
        String filename = file.getOriginalFilename();
        String fileExt = FilenameUtils.getExtension(filename);
        return prefixName + UUID.randomUUID().toString() + "." + fileExt;
    }

    public enum UploadType {
        ORDER, SUBSCRIPTION, CUSTOMER
    }
}
