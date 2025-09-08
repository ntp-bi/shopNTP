package com.ntp.be.controllers;

import com.ntp.be.auth.dto.OrderResponse;
import com.ntp.be.dto.OrderDetails;
import com.ntp.be.dto.OrderRequest;
import com.ntp.be.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) throws Exception {
        OrderResponse orderResponse = orderService.createOrder(orderRequest, principal);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDetails>> getOrderByUser(Principal principal) {
        List<OrderDetails> orderDetails = orderService.getOrdersByUser(principal.getName());
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID id, Principal principal) {
        orderService.cancelOrder(id, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-payment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody Map<String, String> map) {
        Map<String, String> response = orderService.updateStatus(map.get("paymentIntent"), map.get("status"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
