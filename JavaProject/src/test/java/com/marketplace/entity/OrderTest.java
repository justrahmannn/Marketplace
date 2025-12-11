package com.marketplace.entity;




class OrderTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Order order = new Order();

        // Assert
        assertNotNull(order);
        assertNull(order.getId());
        assertNull(order.getCustomer());
        assertNull(order.getProduct());
        assertNull(order.getTotalAmount());
        assertNull(order.getCount());
        assertNull(order.getStatus());
        assertNull(order.getRejectReason());
        assertNull(order.getCreatedAt());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Order order = new Order();
        Long id = 1L;

        // Act
        order.setId(id);

        // Assert
        assertEquals(id, order.getId());
    }

    @Test
    void testSetAndGetCustomer() {
        // Arrange
        Order order = new Order();
        Customer customer = new Customer();

        // Act
        order.setCustomer(customer);

        // Assert
        assertEquals(customer, order.getCustomer());
    }

    @Test
    void testSetAndGetProduct() {
        // Arrange
        Order order = new Order();
        Product product = new Product();

        // Act
        order.setProduct(product);

        // Assert
        assertEquals(product, order.getProduct());
    }

    @Test
    void testSetAndGetTotalAmount() {
        // Arrange
        Order order = new Order();
        BigDecimal totalAmount = new BigDecimal("100.50");

        // Act
        order.setTotalAmount(totalAmount);

        // Assert
        assertEquals(totalAmount, order.getTotalAmount());
    }

    @Test
    void testSetAndGetCount() {
        // Arrange
        Order order = new Order();
        Integer count = 5;

        // Act
        order.setCount(count);

        // Assert
        assertEquals(count, order.getCount());
    }

    @Test
    void testSetAndGetStatus() {
        // Arrange
        Order order = new Order();
        Order.OrderStatus status = Order.OrderStatus.ACCEPTED;

        // Act
        order.setStatus(status);

        // Assert
        assertEquals(status, order.getStatus());
    }

    @Test
    void testSetAndGetRejectReason() {
        // Arrange
        Order order = new Order();
        String rejectReason = "Out of stock";

        // Act
        order.setRejectReason(rejectReason);

        // Assert
        assertEquals(rejectReason, order.getRejectReason());
    }

    @Test
    void testSetAndGetCreatedAt() {
        // Arrange
        Order order = new Order();}}