public List<OrderDTO> findOrdersWithItems() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<OrderDTO> query = cb.createQuery(OrderDTO.class);
    Root<Order> orderRoot = query.from(Order.class);
    Join<Order, OrderItem> itemsJoin = orderRoot.join("items", JoinType.LEFT);

    // Group by orderRoot to handle the OneToMany relationship
    query.groupBy(orderRoot.get("id"));

    // Construct subquery to collect OrderItems
    Subquery<OrderItemDTO> itemSubquery = query.subquery(OrderItemDTO.class);
    Root<OrderItem> orderItemRoot = itemSubquery.from(OrderItem.class);
    itemSubquery.select(cb.construct(OrderItemDTO.class, 
        orderItemRoot.get("productName"), 
        orderItemRoot.get("quantity")
    ));
    itemSubquery.where(cb.equal(orderItemRoot.get("order"), orderRoot));

    // Main query to construct OrderDTO
    query.select(cb.construct(OrderDTO.class,
        orderRoot.get("id"),
        orderRoot.get("customerName"),
        cb.selectSet(itemSubquery)
    ));

    return entityManager.createQuery(query).getResultList();
}