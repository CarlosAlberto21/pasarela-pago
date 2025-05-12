package com.example.prueba_stripe.controller;

import com.example.prueba_stripe.dto.ProductRequest;
import com.example.prueba_stripe.dto.StripeResponse;
import com.example.prueba_stripe.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/v1")
public class ProductCheckoutController {

    private StripeService stripeService;

    public  ProductCheckoutController(StripeService stripeService){
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest){
       StripeResponse stripeResponse= stripeService.checkoutProducts(productRequest);
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(stripeResponse);
    }

}
