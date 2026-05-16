package com.uade.supermercado.patterns.strategy;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MercadoPagoPago implements MetodoPago {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Override
    public ResultadoPago procesarPago(BigDecimal monto, String referenciaPedido) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Compra E-Market - Pedido " + referenciaPedido)
                    .quantity(1)
                    .unitPrice(monto)
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(item))
                    .externalReference(referenciaPedido)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return new ResultadoPago(true, preference.getId(),
                    "Preferencia Mercado Pago creada exitosamente", MetodoPagoEnum.MERCADO_PAGO);

        } catch (MPApiException e) {
            return new ResultadoPago(false, null,
                    "Error API Mercado Pago: " + e.getApiResponse().getContent(), MetodoPagoEnum.MERCADO_PAGO);
        } catch (MPException e) {
            return new ResultadoPago(false, null,
                    "Error al conectar con Mercado Pago: " + e.getMessage(), MetodoPagoEnum.MERCADO_PAGO);
        }
    }

    @Override
    public MetodoPagoEnum getTipo() {
        return MetodoPagoEnum.MERCADO_PAGO;
    }
}
