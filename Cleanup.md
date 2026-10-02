# Migration Project Cleanup Tasks

**Rules & Guidelines:**
- Code must be updated to Java 25.
- Framework must be updated to Spring Boot 4.1.1.
- All libraries must be updated to their latest stable versions.
- No anti-patterns are allowed.
- Ignore the `resource_setup` folder completely.
- After the Cleanup.md is done for the entire project then we can do changes in code.

---

### .
- `./docker-compose.yml`: Check services and update all image versions to the latest stable

### NexusAdminPanel/src/pages
- `NexusAdminPanel/src/pages/Dashboard.tsx`: Use let/const instead of var

### NexusAdminPanel/src/pages/pms
- `NexusAdminPanel/src/pages/pms/ProductAdd.tsx`: Avoid using "any" type
- `NexusAdminPanel/src/pages/pms/ProductList.tsx`: Avoid using "any" type
- `NexusAdminPanel/src/pages/pms/ProductUpdate.tsx`: Avoid using "any" type

### NexusCore/nexus-admin/src/main/java/com/nexusengine/core/component
- `NexusCore/nexus-admin/src/main/java/com/nexusengine/core/component/ProductSyncSender.java`: Field injection (use constructor injection)

### NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl
- `NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductServiceImpl.java`: Empty catch block
- `NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsSkuStockServiceImpl.java`: Empty catch block
- `NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/SmsFlashPromotionServiceImpl.java`: Field injection (use constructor injection), Use java.time instead of java.util.Date

### NexusCore/nexus-ai/src/main/java/com/nexusengine/core/ai/service
- `NexusCore/nexus-ai/src/main/java/com/nexusengine/core/ai/service/AbstractNexusChatService.java`: Field injection (use constructor injection)

### NexusCore/nexus-ai/src/test/java/com/nexusengine/core/ai/service
- `NexusCore/nexus-ai/src/test/java/com/nexusengine/core/ai/service/AbstractNexusChatServiceTest.java`: Empty catch block

### NexusCore/nexus-application/src/main/java/com/nexusengine/core/application/config
- `NexusCore/nexus-application/src/main/java/com/nexusengine/core/application/config/UnifiedSecurityConfig.java`: Empty catch block

### NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/log
- `NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/log/WebLogAspect.java`: Empty catch block

### NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/service/impl
- `NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/service/impl/RedisServiceImpl.java`: Field injection (use constructor injection)

### NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/AiChatConversationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/FlywayMigrationTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsCartItemRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsCompanyAddressRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsOrderItemRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsOrderOperateHistoryRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsOrderRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsOrderReturnApplyRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsOrderReturnReasonRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsOrderSettingRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OmsPaymentTransactionRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/OutboxEventRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsBrandRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsCommentReplyRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsCommentRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductAttributeCategoryRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductAttributeRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductAttributeValueRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductCategoryAttributeRelationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductCategoryRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductEmbeddingRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductFullReductionRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductLadderRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductOperateLogRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsProductVerifyRecordRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsReviewMediaRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsReviewRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/PmsSkuStockRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsCouponHistoryRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsCouponProductCategoryRelationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsCouponProductRelationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsCouponRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsFlashPromotionLogRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsFlashPromotionProductRelationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsFlashPromotionRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsFlashPromotionSessionRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/SmsHomeAdvertiseRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsAdminLoginLogRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsAdminRepositoryTest.java`: Field injection (use constructor injection), Use java.time instead of java.util.Date
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsAdminRoleRelationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsMemberLoginLogRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsMemberProductCategoryRelationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsMemberReceiveAddressRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsMemberRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsMenuRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsResourceCategoryRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsResourceRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsRoleMenuRelationRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsRoleRepositoryTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-data-jpa/src/test/java/com/nexusengine/core/repository/UmsRoleResourceRelationRepositoryTest.java`: Field injection (use constructor injection)

### NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/component
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/component/CancelOrderReceiver.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/component/CancelOrderSender.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/component/OrderTimeOutCancelTask.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/component/OutboxEventProcessor.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/component/RateLimitInterceptor.java`: Field injection (use constructor injection)

### NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/config
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/config/RateLimitConfig.java`: Field injection (use constructor injection)

### NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/ChatbotController.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/HomeController.java`: Empty catch block
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/TestController.java`: Empty catch block
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/UmsMemberController.java`: Use logger instead of printStackTrace

### NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/HomeDao.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/PortalProductDao.java`: Field injection (use constructor injection)

### NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java`: Use logger instead of printStackTrace
- `NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsProductSemanticSearchServiceImpl.java`: Avoid Thread.sleep (except in tests)

### NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration/CouponConcurrencyIntegrationTest.java`: Field injection (use constructor injection), Empty catch block
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration/OrderLifecycleIntegrationTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration/OutboxEventIntegrationTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration/PmsProductSemanticSearchServiceIntegrationTest.java`: Field injection (use constructor injection)

### NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/slice/controller
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/slice/controller/OmsPortalOrderControllerTest.java`: Field injection (use constructor injection)
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/slice/controller/UmsMemberControllerTest.java`: Field injection (use constructor injection)

### NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/slice/repository
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/slice/repository/OmsOrderRepositorySliceTest.java`: Field injection (use constructor injection)

### NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/unit
- `NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/unit/OmsPortalOrderServiceImplTest.java`: Empty catch block

### NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/component
- `NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/component/ProductChangeConsumer.java`: Field injection (use constructor injection)

### NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/dao
- `NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/dao/EsProductDao.java`: Field injection (use constructor injection)

### NexusCore/nexus-search/src/test/java/com/nexusengine/core/search
- `NexusCore/nexus-search/src/test/java/com/nexusengine/core/search/NexusSearchApplicationTests.java`: Field injection (use constructor injection), Use logger instead of System.out.println

### NexusCore/nexus-security/src/main/java/com/nexusengine/core/security/config
- `NexusCore/nexus-security/src/main/java/com/nexusengine/core/security/config/SecurityConfig.java`: Field injection (use constructor injection)

### NexusCore/nexus-security/src/main/java/com/nexusengine/core/security/util
- `NexusCore/nexus-security/src/main/java/com/nexusengine/core/security/util/JwtTokenUtil.java`: Use java.time instead of java.util.Date

### NexusFrontendWeb/src/components
- `NexusFrontendWeb/src/components/BannerCarousel.tsx`: Avoid using "any" type
- `NexusFrontendWeb/src/components/ChatWidget.tsx`: Avoid using "any" type

### NexusFrontendWeb/src/views
- `NexusFrontendWeb/src/views/Checkout.tsx`: Avoid using "any" type
- `NexusFrontendWeb/src/views/ProductDetail.tsx`: Avoid using "any" type
- `NexusFrontendWeb/src/views/Profile.tsx`: Avoid using "any" type
\n### NexusAdminPanel\n- `NexusAdminPanel/package.json`: Check and update all libraries to their latest stable versions\n\n### NexusFrontendWeb\n- `NexusFrontendWeb/package.json`: Check and update all libraries to their latest stable versions
