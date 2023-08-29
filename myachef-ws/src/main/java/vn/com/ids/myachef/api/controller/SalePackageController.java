package vn.com.ids.myachef.api.controller;

//@RestController
//@RequestMapping("/api/sale-package")
//@Slf4j
//@Validated
public class SalePackageController {

//    @Autowired
//    private SalePackageService salePackageService;
//
//    @Autowired
//    private SalePackageConverter salePackageConverter;
//
//    @Autowired
//    private SalePackageDetailService salePackageDetailService;
//
//    @Operation(summary = "search by criteria")
//    @GetMapping("/search")
//    public Page<SalePackageDTO> getByCriteria(@ParameterObject SalePackageCriteria salePackageCriteria) {
//        log.info("------------------ Search, pagination - START ----------------");
//        Page<SalePackageModel> page = salePackageService.search(salePackageCriteria);
//        List<SalePackageDTO> salePackageDTOs = salePackageConverter.toBasicDTOs(page.getContent());
//        Pageable pageable = PageRequest.of(salePackageCriteria.getPageIndex(), salePackageCriteria.getPageSize());
//        log.info("------------------ Search, pagination - END ----------------");
//        return new PageImpl<>(salePackageDTOs, pageable, page.getTotalElements());
//    }
//
//    @Operation(summary = "Find by id")
//    @GetMapping("/{id}")
//    public SalePackageDTO findById(@PathVariable Long id) {
//        log.info("------------------ Find by id - START ----------------");
//        SalePackageModel salePackageModel = salePackageService.findOne(id);
//        if (salePackageModel == null) {
//            throw new ResourceNotFoundException("Not found sale package with id: " + id);
//        }
//        log.info("------------------ Find by id - END ----------------");
//        return salePackageConverter.toDTO(salePackageModel);
//    }
//
//    @Operation(summary = "Create")
//    @PostMapping
//    @Validated(OnCreate.class)
//    public SalePackageDTO create(@RequestBody @Valid SalePackageDTO salePackageDTO) {
//        log.info("------------------ Create - START ----------------");
//        return salePackageService.create(salePackageDTO);
//    }
//
//    @Operation(summary = "Update")
//    @PutMapping("/{id}")
//    @Validated(OnUpdate.class)
//    public SalePackageDTO update(@PathVariable Long id, @RequestBody @Valid SalePackageDTO salePackageDTO) {
//        log.info("------------------ Update - START ----------------");
//        SalePackageModel salePackageModel = salePackageService.findOne(id);
//        if (salePackageModel == null) {
//            throw new ResourceNotFoundException("Not found sale package with id: " + id);
//        }
//        return salePackageService.update(salePackageModel, salePackageDTO);
//    }
//
//    @Operation(summary = "Delete")
//    @DeleteMapping
//    public void delete(@RequestParam List<Long> ids) {
//        log.info("ids: {}", ids);
//        if (!CollectionUtils.isEmpty(ids)) {
//            salePackageService.updateStatusInActiveByIds(ids);
//        }
//
//        log.info("------------------ Delete - END ----------------");
//    }
//
//    @Operation(summary = "Buy")
//    @PostMapping("/buy")
//    @Validated(OnCreate.class)
//    public SalePackageDetailDTO buy(@RequestBody @Valid SalePackageDetailDTO salePackageDetailDTO) {
//        log.info("------------------ Buy - START ----------------");
//        SalePackageDetailDTO result = salePackageDetailService.create(salePackageDetailDTO);
//        log.info("------------------ Buy - END ----------------");
//        return result;
//    }
//
//    @Operation(summary = "Show sale package by a customer")
//    @GetMapping("/customer/{id}")
//    @Validated(OnCreate.class)
//    public List<SalePackageCustomerDTO> showByCustomer(@PathVariable Long customerId) {
//        log.info("------------------ ShowByCustomer - START ----------------");
//        return salePackageDetailService.showByCustomer(customerId);
//    }
//
//    @Operation(summary = "Show customer bought sale package by sale package id")
//    @GetMapping("/customer-bought/{id}")
//    @Validated(OnCreate.class)
//    public CustomerOfSalePackageDTO showCustomerBoughtSalePackage(@PathVariable Long id) {
//        log.info("------------------ ShowCustomerBoughtSalePackage - START ----------------");
//        return salePackageDetailService.showCustomerBoughtPackage(id);
//    }

}
