package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(Long id, Long productId, int quantity) {
        Order order = new Order();
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            return new Order();
        }
        order.setUser(userOptional.get());
        order.setStatus("Pending");
        order.setCreatedAt(LocalDateTime.now());
        OrderItem item = new OrderItem();
        Float totalPrice = (float) 0;
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null && product.getStock() >= quantity) {
                item.setOrder(order);
                item.setQuantity(quantity);
                item.setPrice(product.getPrice() * quantity);
                totalPrice = totalPrice+item.getPrice();
                order.setTotalPrice(totalPrice);
                orderRepository.save(order);
                orderItemRepository.save(item);

                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            }

        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}
