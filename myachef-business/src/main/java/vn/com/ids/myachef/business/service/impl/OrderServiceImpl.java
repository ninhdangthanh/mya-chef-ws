package vn.com.ids.myachef.business.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.OrderConverter;
import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DinnerTableService;
import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.OrderCriteria;
import vn.com.ids.myachef.dao.criteria.builder.OrderSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.OrderDetailStatus;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.DinnerTableModel;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.model.UserModel;
import vn.com.ids.myachef.dao.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl extends AbstractService<OrderModel, Long> implements OrderService {

	private OrderRepository orderRepository;
	
	@Autowired
	private OrderConverter orderConverter;
	
	@Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;
    
    @Autowired
    private DinnerTableService dinnerTableService;
    
    @Autowired
    private DishService dishService;
    
    @Autowired
    private UserService userService;

	protected OrderServiceImpl(OrderRepository orderRepository) {
		super(orderRepository);
		this.orderRepository = orderRepository;
	}

	public Specification<OrderModel> buildSpecification(OrderCriteria orderCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new OrderSpecificationBuilder(root, criteriaBuilder) //
                .setStatus(orderCriteria.getStatus()) //
                .setTotalPayment(orderCriteria.getTotalPayment()) //
                .setImagePayment(orderCriteria.getImagePayment())
                .build();
    }
	
    @Override
    public Page<OrderModel> findAll(OrderCriteria orderCriteria) {
        Specification<OrderModel> specification = buildSpecification(orderCriteria);
        Pageable pageable = buildPageable(orderCriteria);
        return orderRepository.findAll(specification, pageable);
    }

    @Override
    public OrderDTO create(@Valid OrderDTO orderDTO) {
        OrderModel orderModel = orderConverter.toBasicModel(orderDTO);
        
        // dinner table //
        if(orderDTO.getDinnerTableId() == null || orderDTO.getDinnerTableId() <= 0) {
            throw new BadRequestException("Dinner table id must be not null and <= 0");
        }
        
        DinnerTableModel dinnerTableModel = dinnerTableService.findOne(orderDTO.getDinnerTableId());
        if(dinnerTableModel == null) {
            throw new BadRequestException("Not found dinner table with id: " + orderDTO.getDinnerTableId());
        }
        if(dinnerTableModel.getStatus() == Status.ACTIVE) {
            throw new BadRequestException("Dinner table already used");
        }
        dinnerTableModel.setStatus(Status.ACTIVE);
        orderModel.setDinnerTable(dinnerTableModel);
        
        // user //
        if(orderDTO.getUserId() == null || orderDTO.getUserId() <= 0) {
            throw new BadRequestException("User id must be not null and <= 0");
        }
        UserModel userModel = userService.findOne(orderDTO.getUserId());
        if(userModel == null) {
            throw new ResourceNotFoundException("Not found user with id: " + orderDTO.getUserId());
        }
        orderModel.setUser(userModel);
        
        // dish //
        if(!CollectionUtils.isEmpty(orderDTO.getDishIds())) {
            List<DishModel> dishModels = dishService.findAllById(orderDTO.getDishIds());
            if(!CollectionUtils.isEmpty(dishModels)) {
                List<OrderDetailModel> orderDetailModels = dishModels.stream().map(dish -> {
                    OrderDetailModel orderDetailModel = new OrderDetailModel();
                    orderDetailModel.setDish(dish);
                    orderDetailModel.setStatus(OrderDetailStatus.NOT_FINISHED);
                    return orderDetailModel;
                }).collect(Collectors.toList());
                orderModel.setOrderDetails(orderDetailModels);
            }
        }
        
        orderModel.setStatus(OrderStatus.UNPAID);
        calculate(orderModel);
        orderModel = save(orderModel);
        return orderConverter.toBasicDTO(orderModel);
    }

    @Override
    public OrderDTO update(@Valid OrderDTO orderDTO, OrderModel orderModel, MultipartFile image) {
        if (image != null) {
            if (StringUtils.hasText(orderModel.getImagePayment())) {
                fileStorageService.delete(applicationConfig.getFullIngredientPath() + orderModel.getImagePayment());
            }

            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullOrderPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullOrderPath(), prefixName), generatedName, image);
            orderModel.setImagePayment(generatedName);
        }

        orderConverter.mapDataToUpdate(orderModel, orderDTO);

        if(orderDTO.getDinnerTableId() != null && orderDTO.getDinnerTableId() > 0) {
            DinnerTableModel dinnerTableModel = dinnerTableService.findOne(orderDTO.getDinnerTableId());
            if(dinnerTableModel == null) {
                throw new ResourceNotFoundException("Not found dinner table with id: " + orderDTO.getDinnerTableId());
            }
            orderModel.setDinnerTable(dinnerTableModel);
        }
        
        if(orderDTO.getUserId() != null && orderDTO.getUserId() > 0) {
            UserModel userModel = userService.findOne(orderDTO.getUserId());
            if(userModel == null) {
                throw new ResourceNotFoundException("Not found user with id: " + orderDTO.getUserId());
            }
            orderModel.setUser(userModel);
        }

        calculate(orderModel);
        orderModel = save(orderModel);

        return orderConverter.toBasicDTO(orderModel);
    }
    
    public void calculate(OrderModel orderModel) {
        if(!CollectionUtils.isEmpty(orderModel.getOrderDetails())) {
            List<DishModel> dishs = orderModel.getOrderDetails().stream().map(OrderDetailModel::getDish).collect(Collectors.toList());
            Double totalAmount = dishs.stream().mapToDouble(DishModel::getPrice).sum();
            orderModel.setTotalPayment(totalAmount);
        }
    }

}
