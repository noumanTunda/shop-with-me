package com.tundalabs.store.payments;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.tundalabs.store.entities.Order;
import com.tundalabs.store.entities.OrderItem;
import com.tundalabs.store.entities.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway{
//    private final PaymentIntent paymentIntent;

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl+ "/checkout-sucess?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl+ "/checkout-cancel")
                    .putMetadata("order_id", order.getId().toString())
                    .putExtraParam("managed_payments", Map.of("enabled", false));//to disable managed payments

            order.getItems().forEach(item-> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        }
        catch (StripeException ex){
            throw new PaymentException();
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            return switch (event.getType()){
                case "payment_intent.succeeded" ->
                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));

                case "payment_intent.payment_failed" ->
                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));

                default ->
                        Optional.empty();
            };

        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid Signature");
        }
    }

    private Long extractOrderId(Event event){
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could Not Deserialize Stripe Event. Check SDK and API version"));

        var paymentIntent = (PaymentIntent)stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("tzs")
                .setUnitAmountDecimal(
                    item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
