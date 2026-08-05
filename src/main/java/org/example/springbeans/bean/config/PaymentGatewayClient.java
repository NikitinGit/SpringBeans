package org.example.springbeans.bean.config;

import org.springframework.stereotype.Component;

@Component
@ExternalEndpoint(url = "https://api.example.com/pay")
public class PaymentGatewayClient {
}
