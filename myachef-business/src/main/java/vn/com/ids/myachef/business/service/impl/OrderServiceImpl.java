package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
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
import vn.com.ids.myachef.business.dto.OrderDetailDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DinnerTableService;
import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.business.service.IngredientService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.OrderCriteria;
import vn.com.ids.myachef.dao.criteria.builder.OrderSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.OrderDetailStatus;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.DinnerTableModel;
import vn.com.ids.myachef.dao.model.DishDetailModel;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.IngredientModel;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.model.UserModel;
import vn.com.ids.myachef.dao.repository.OrderDetailRepository;
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

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    protected OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    public Specification<OrderModel> buildSpecification(OrderCriteria orderCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new OrderSpecificationBuilder(root, criteriaBuilder) //
                .setStatus(orderCriteria.getStatus()) //
                .setTotalPayment(orderCriteria.getTotalPayment()) //
                .setImagePayment(orderCriteria.getImagePayment()).build();
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
        if (orderDTO.getDinnerTableId() == null || orderDTO.getDinnerTableId() <= 0) {
            throw new BadRequestException("Dinner table id must be not null and <= 0");
        }

        DinnerTableModel dinnerTableModel = dinnerTableService.findOne(orderDTO.getDinnerTableId());
        if (dinnerTableModel == null) {
            throw new BadRequestException("Not found dinner table with id: " + orderDTO.getDinnerTableId());
        }
        if (dinnerTableModel.getStatus() == Status.ACTIVE) {
            throw new BadRequestException("Dinner table already used");
        }
        dinnerTableModel.setStatus(Status.ACTIVE);
        orderModel.setDinnerTable(dinnerTableModel);
        
        dinnerTableModel.setEnoughtFood(false);
        dinnerTableService.save(dinnerTableModel);

        // user //
        if (orderDTO.getUserId() == null || orderDTO.getUserId() <= 0) {
            throw new BadRequestException("User id must be not null and <= 0");
        }
        UserModel userModel = userService.findOne(orderDTO.getUserId());
        if (userModel == null) {
            throw new ResourceNotFoundException("Not found user with id: " + orderDTO.getUserId());
        }
        orderModel.setUser(userModel);

        orderModel.setStatus(OrderStatus.UNPAID);
        calculate(orderModel);
        orderModel = save(orderModel);
        return orderConverter.toDTO(orderModel);
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

        if (orderDTO.getDinnerTableId() != null && orderDTO.getDinnerTableId() > 0) {
            DinnerTableModel dinnerTableModel = dinnerTableService.findOne(orderDTO.getDinnerTableId());
            if (dinnerTableModel == null) {
                throw new ResourceNotFoundException("Not found dinner table with id: " + orderDTO.getDinnerTableId());
            }
            orderModel.setDinnerTable(dinnerTableModel);
        }

        if (orderDTO.getUserId() != null && orderDTO.getUserId() > 0) {
            UserModel userModel = userService.findOne(orderDTO.getUserId());
            if (userModel == null) {
                throw new ResourceNotFoundException("Not found user with id: " + orderDTO.getUserId());
            }
            orderModel.setUser(userModel);
        }

        calculate(orderModel);
        orderModel = save(orderModel);

        return orderConverter.toBasicDTO(orderModel);
    }

    public void calculate(OrderModel orderModel) {
        if (!CollectionUtils.isEmpty(orderModel.getOrderDetails())) {
            List<DishModel> dishs = orderModel.getOrderDetails().stream().map(OrderDetailModel::getDish).collect(Collectors.toList());
            Double totalAmount = dishs.stream().mapToDouble(DishModel::getPrice).sum();
            orderModel.setTotalPayment(totalAmount);
        }
    }

    @Override
    public String addDish(OrderModel orderModel, Long dishId) {
        DishModel dishModel = dishService.findOne(dishId);
        if (dishModel == null) {
            throw new BadRequestException("Not found dish with id: " + dishId);
        }
        // xử lý trừ nguyên liệu - start //
        List<DishDetailModel> dishDetailModels = dishModel.getDishDetails();
        if (!CollectionUtils.isEmpty(dishDetailModels)) {
            List<IngredientModel> ingredientsWillChangeQuantity = new ArrayList<>();
            for (DishDetailModel dishDetailModel : dishDetailModels) {
                IngredientModel ingredientModel = dishDetailModel.getIngredient();
                if (ingredientModel.getQuantity() < dishDetailModel.getQuantity()) {
                    // thiếu nguyên liệu
                    ingredientModel.setStatus(Status.IN_ACTIVE);
                    ingredientService.save(ingredientModel);
                    throw new BadRequestException("Món ăn này hiện tại không đủ nguyên liệu!");
                } else {
                    // đủ nguyên liệu
                    ingredientModel.setQuantity(ingredientModel.getQuantity() - dishDetailModel.getQuantity());
                    ingredientsWillChangeQuantity.add(ingredientModel);
                }
            }
            if (!CollectionUtils.isEmpty(ingredientsWillChangeQuantity)) {
                ingredientService.saveAll(ingredientsWillChangeQuantity);
            }
        }
        // xử lý trừ nguyên liệu - end //

        // xử lý thêm vào món ăn sẵn có trong hóa đơn hoặc tạo món ăn mới //
        List<OrderDetailModel> orderDetailModels = orderDetailRepository.findByOrderIdAndDishId(orderModel.getId(), dishId);
        if (CollectionUtils.isEmpty(orderDetailModels)) {
            OrderDetailModel orderDetailModel = new OrderDetailModel();
            orderDetailModel.setDish(dishModel);
            orderDetailModel.setStatus(OrderDetailStatus.NOT_FINISHED);
            orderDetailModel.setQuantity(1);
            orderModel.getOrderDetails().add(orderDetailModel);
            orderDetailModel.setOrder(orderModel);
        } else {
            OrderDetailModel orderDetailModel = orderDetailModels.get(0);
            orderDetailModel.setQuantity(orderDetailModel.getQuantity() + 1);
        }
        
        // xử lý chuyển trạng thái bàn sang chưa đủ món ăn ở trên bàn
        DinnerTableModel dinnerTableModel = orderModel.getDinnerTable();
        dinnerTableModel.setEnoughtFood(false);
        dinnerTableService.save(dinnerTableModel);

        save(orderModel);

        return "Món ăn này đã được thêm vào hóa đơn!";
    }

    @Override
    public String removeDish(OrderModel orderModel, Long dishId) {
        List<OrderDetailModel> orderDetailModels = orderDetailRepository.findByOrderIdAndDishId(orderModel.getId(), dishId);

        if (CollectionUtils.isEmpty(orderDetailModels)) {
            throw new BadRequestException("Món ăn này hiện không sẵn có trong hóa đơn!");
        }

        OrderDetailModel orderDetailModel = orderDetailModels.get(0);

        if (orderDetailModel.getStatus() != OrderDetailStatus.NOT_FINISHED) {
            throw new BadRequestException("Món ăn này đã được chế biến nên không thể xóa khỏi hóa đơn!");
        }

        DishModel dishModel = orderDetailModel.getDish();

        List<IngredientModel> reverseIngredients = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dishModel.getDishDetails())) {
            for (DishDetailModel dishDetailModel : dishModel.getDishDetails()) {
                IngredientModel ingredientModel = dishDetailModel.getIngredient();
                ingredientModel.setQuantity(ingredientModel.getQuantity() + dishDetailModel.getQuantity());
                reverseIngredients.add(ingredientModel);
            }
            ingredientService.saveAll(reverseIngredients);
        }

        orderModel.getOrderDetails().remove(orderDetailModel);

        save(orderModel);

        return "Món ăn này đã được xóa khỏi hóa đơn!";
    }

    @Override
    public OrderDTO completeOrder(OrderModel orderModel) {
        orderModel.setStatus(OrderStatus.PAID);
        
        if(!CollectionUtils.isEmpty(orderModel.getOrderDetails())) {
            for (OrderDetailModel orderDetailModel : orderModel.getOrderDetails()) {
                orderDetailModel.setStatus(OrderDetailStatus.LEAVED_KITCHEN);
                orderDetailModel.setOrder(orderModel);
            }
        }
        
        orderModel.getDinnerTable().setStatus(Status.IN_ACTIVE);
        
        DinnerTableModel dinnerTableModel = orderModel.getDinnerTable();
        dinnerTableModel.setEnoughtFood(false);
        dinnerTableService.save(dinnerTableModel);
        
        
        orderModel = save(orderModel);
        
        return orderConverter.toDTO(orderModel);
    }

    @Override
    public OrderDTO uploadImagePayment(OrderModel orderModel, MultipartFile image) {
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
        
        orderModel = save(orderModel);
        
        return orderConverter.toDTO(orderModel);
    }

    @Override
    public OrderDTO confirmBankPayment(OrderModel orderModel) {
        orderModel.setIsPaymentWithBanking(true);
        
        orderModel = save(orderModel);
        
        return orderConverter.toDTO(orderModel);
    }
    
    @Override
    public OrderModel findOrderExistingByDinnerTableId(Long dinnerTableId) {
        return orderRepository.findOrderExistingByDinnerTableId(dinnerTableId);
    }
    
    @Override
    public String changeStatusOneFood(OrderDetailModel orderDetailModel, OrderDetailDTO orderDetailDTO) {
        if(orderDetailDTO.getStatus() == null) {
            throw new BadRequestException("You must add status into order detail to using this API");
        }
        orderDetailModel.setStatus(orderDetailDTO.getStatus());
        
        orderDetailRepository.save(orderDetailModel);
        
        
        // kiểm tra bàn đã đủ món ăn chưa
        boolean enoughFood = true;
        
        OrderModel orderModel = orderDetailModel.getOrder();
        if(!CollectionUtils.isEmpty(orderModel.getOrderDetails())) {
            for (OrderDetailModel orderDetailModelItem : orderModel.getOrderDetails()) {
                if(orderDetailModelItem.getStatus() != OrderDetailStatus.LEAVED_KITCHEN) {
                    enoughFood = false;
                }
            }
        }
        
        if(enoughFood) {
            DinnerTableModel dinnerTableModel = orderModel.getDinnerTable();
            dinnerTableModel.setEnoughtFood(true);
            dinnerTableService.save(dinnerTableModel);
        }
        
        return null;
    }

}
