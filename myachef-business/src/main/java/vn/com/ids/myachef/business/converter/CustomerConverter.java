package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.CustomerDTO;
import vn.com.ids.myachef.dao.model.CustomerModel;

@Component
public class CustomerConverter {

    @Autowired
    private ModelMapper mapper;

    public CustomerDTO toBasicDTO(CustomerModel customerModel) {
        return mapper.map(customerModel, CustomerDTO.class);
    }

    public List<CustomerDTO> toBasicDTOs(List<CustomerModel> customerModels) {
        if (CollectionUtils.isEmpty(customerModels)) {
            return new ArrayList<>();
        }

        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (CustomerModel customerModel : customerModels) {
            customerDTOs.add(toBasicDTO(customerModel));
        }

        return customerDTOs;
    }

    public CustomerDTO toDTO(CustomerModel customerModel) {
        return toBasicDTO(customerModel);
    }

    public List<CustomerDTO> toDTOs(List<CustomerModel> customerModels) {
        if (CollectionUtils.isEmpty(customerModels)) {
            return new ArrayList<>();
        }

        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (CustomerModel customerModel : customerModels) {
            customerDTOs.add(toDTO(customerModel));
        }

        return customerDTOs;
    }

    public CustomerModel toModel(CustomerDTO customerDTO) {
        CustomerModel customerModel = mapper.map(customerDTO, CustomerModel.class);
        return customerModel;
    }

    public void mapDataToUpdate(CustomerModel customerModel, CustomerDTO customerDTO) {
        if (customerDTO.getFullName() != null) {
            customerModel.setFullName(customerDTO.getFullName());
        }

        if (customerDTO.getBirthday() != null) {
            customerModel.setBirthday(customerDTO.getBirthday());
        }

        if (customerDTO.getStatus() != null) {
            customerModel.setStatus(customerDTO.getStatus());
        }

        if (customerDTO.getPhoneNumber() != null) {
            customerModel.setPhoneNumber(customerDTO.getPhoneNumber());
        }

        if (customerDTO.getCity() != null) {
            customerModel.setCity(customerDTO.getCity());
        }

        if (customerDTO.getCityId() != null) {
            customerModel.setCityId(customerDTO.getCityId());
        }

        if (customerDTO.getDistrict() != null) {
            customerModel.setDistrict(customerDTO.getDistrict());
        }

        if (customerDTO.getDistrictId() != null) {
            customerModel.setDistrictId(customerDTO.getDistrictId());
        }

        if (customerDTO.getWard() != null) {
            customerModel.setWard(customerDTO.getWard());
        }

        if (customerDTO.getWardId() != null) {
            customerModel.setWardId(customerDTO.getWardId());
        }

        if (customerDTO.getAddress() != null) {
            customerModel.setAddress(customerDTO.getAddress());
        }

        if (customerDTO.getChildName() != null) {
            customerModel.setChildName(customerDTO.getChildName());
        }

        if (customerDTO.getChildGender() != null) {
            customerModel.setChildGender(customerDTO.getChildGender());
        }

        if (customerDTO.getChildBirthday() != null) {
            customerModel.setChildBirthday(customerDTO.getChildBirthday());
        }

        if (customerDTO.getIsFollowOA() != null) {
            customerModel.setIsFollowOA(customerDTO.getIsFollowOA());
        }

        customerModel.setSearchText(buildSearchText(customerModel));

    }

    private String buildSearchText(CustomerModel customerModel) {
        StringBuilder builder = new StringBuilder();
        builder.append(customerModel.getFullName());
        builder.append(" ");
        builder.append(customerModel.getPhoneNumber());
        return builder.toString();

    }
}
