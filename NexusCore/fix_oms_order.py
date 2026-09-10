import re

file_path = '/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/OmsPortalOrderService.java'

with open(file_path, 'r') as f:
    content = f.read()

content = content.replace("/**\n */\npublic interface OmsPortalOrderService", "/**\n * Service interface for managing portal orders.\n * Handles order creation, payment success, cancellation, and retrieval.\n */\npublic interface OmsPortalOrderService", 1)

replacements = [
    ("ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);", "/**\n     * Generates a confirmation result for a list of cart items.\n     *\n     * @param cartIds the list of cart item IDs\n     * @return the confirmation order result containing order details\n     */"),
    ("@Transactional\n    Map<String, Object> generateOrder(OrderParam orderParam);", "/**\n     * Generates a new order based on the provided parameters.\n     *\n     * @param orderParam the order creation parameters\n     * @return a map containing order generation results\n     */"),
    ("@Transactional\n    Integer paySuccess(Long orderId, Integer payType);", "/**\n     * Processes a successful payment for an order.\n     *\n     * @param orderId the unique identifier of the order\n     * @param payType the payment type used\n     * @return the status of the operation\n     */"),
    ("@Transactional\n    Integer cancelTimeOutOrder();", "/**\n     * Cancels all orders that have timed out pending payment.\n     *\n     * @return the number of canceled orders\n     */"),
    ("@Transactional\n    void cancelOrder(Long orderId);", "/**\n     * Cancels a specific order manually.\n     *\n     * @param orderId the unique identifier of the order to cancel\n     */"),
    ("void sendDelayMessageCancelOrder(Long orderId);", "/**\n     * Sends a delayed message to automatically cancel an order if unpaid.\n     *\n     * @param orderId the unique identifier of the order\n     */"),
    ("void confirmReceiveOrder(Long orderId);", "/**\n     * Confirms the receipt of an order by the user.\n     *\n     * @param orderId the unique identifier of the order\n     */"),
    ("CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize);", "/**\n     * Retrieves a paginated list of orders filtered by status.\n     *\n     * @param status the order status filter\n     * @param pageNum the current page number\n     * @param pageSize the number of items per page\n     * @return a paginated list of order details\n     */"),
    ("OmsOrderDetail detail(Long orderId);", "/**\n     * Retrieves the detailed information of a specific order.\n     *\n     * @param orderId the unique identifier of the order\n     * @return the comprehensive order details\n     */"),
    ("void deleteOrder(Long orderId);", "/**\n     * Deletes a specific order from the system.\n     *\n     * @param orderId the unique identifier of the order to delete\n     */"),
    ("@Transactional\n    void paySuccessByOrderSn(String orderSn, Integer payType);", "/**\n     * Processes a successful payment using the order serial number.\n     *\n     * @param orderSn the unique serial number of the order\n     * @param payType the payment type used\n     */")
]

for old, new in replacements:
    content = content.replace(f"    /**\n     */\n    {old}", f"{new}\n    {old}")
    
with open(file_path, 'w') as f:
    f.write(content)

