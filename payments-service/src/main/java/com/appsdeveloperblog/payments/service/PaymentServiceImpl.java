package com.appsdeveloperblog.payments.service;

import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.payments.dao.jpa.entity.PaymentEntity;
import com.appsdeveloperblog.payments.dao.jpa.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final String SAMPLE_CREDIT_CARD_NUMBER = "374245455400126";
    private final PaymentRepository paymentRepository;
    private final CreditCardProcessorRemoteService ccpRemoteService;

    @Override
    public Payment process(Payment payment) {
        BigDecimal totalPrice = payment.productPrice()
                .multiply(new BigDecimal(payment.productQuantity()));
        ccpRemoteService.process(new BigInteger(SAMPLE_CREDIT_CARD_NUMBER), totalPrice);

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setOrderId(payment.orderId());
        paymentEntity.setProductId(payment.productId());
        paymentEntity.setProductPrice(payment.productPrice());
        paymentEntity.setProductQuantity(payment.productQuantity());
        paymentRepository.save(paymentEntity);

        return new Payment(paymentEntity.getId(), payment.orderId(), payment.productId(), payment.productPrice(), payment.productQuantity());
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll().stream()
                .map(e -> new Payment(e.getId(), e.getOrderId(), e.getProductId(), e.getProductPrice(), e.getProductQuantity()))
                .collect(Collectors.toList());
    }
}
