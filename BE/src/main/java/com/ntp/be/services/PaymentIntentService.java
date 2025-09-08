package com.ntp.be.services;

import com.ntp.be.entities.Order;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentIntentService {

    // Tỷ giá 1 USD = 24,000 VND
    private static final BigDecimal EXCHANGE_RATE = new BigDecimal("24000");

    public Map<String, String> createPaymentIntent(Order order) throws StripeException {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("orderId", order.getId().toString());

        // Lấy totalAmount VNĐ
        BigDecimal amountVND = BigDecimal.valueOf(order.getTotalAmount());

        // Convert VNĐ → USD
        BigDecimal amountUSD = amountVND.divide(EXCHANGE_RATE, 2, RoundingMode.HALF_UP);

        // Stripe yêu cầu đơn vị nhỏ nhất (cents)
        long amountInCents = amountUSD.multiply(new BigDecimal(100)).longValue();

        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd") // Stripe không hỗ trợ "vnd"
                .putAllMetadata(metaData)
                .setDescription("Payment for Order " + order.getId())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);

        Map<String, String> map = new HashMap<>();
        map.put("client_secret", paymentIntent.getClientSecret());
        map.put("payment_intent_id", paymentIntent.getId());

        return map;
    }
}
