package com.example.prueba_stripe.service;

import com.example.prueba_stripe.dto.ProductRequest;
import com.example.prueba_stripe.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    @Value("${stripe.secretKey}")
    private String secretKey;


    public StripeResponse checkoutProducts(ProductRequest productRequest){
        Stripe.apiKey=secretKey;

        //Aqui estoy conectadome en la api de stripe para crear parámetros de una sesión de pago de un producto (precio, detalle)
        //Y creando un objeto de tipo Producto, preio y cantidad, para otener sus valores
        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder().
                setName(productRequest.getName()).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder().
                setCurrency(productRequest.getCurrency() == null ? "USD": productRequest.getCurrency())
                .setUnitAmount(productRequest.getAmount())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(productRequest.getQuantity())
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .build();

        com.stripe.model.checkout.Session session = null;

        try {
          session = Session.create(params);
        }catch (StripeException ex){
            System.out.println(ex.getMessage());
        }

        StripeResponse.StripeResponseBuilder builder = StripeResponse.builder();
        builder.status("Success");
        builder.messsage("Payment session created");
        builder.sessionId(session.getId());
        builder.sessionUrl(session.getUrl());
        return builder
                .build();


    }
}
