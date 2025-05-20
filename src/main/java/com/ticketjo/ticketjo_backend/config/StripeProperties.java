package com.ticketjo.ticketjo_backend.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuration pour les propriétés Stripe.
 * Elle récupère les valeurs définies dans application.properties
 * sous le préfixe "stripe".
 * stripe.secret-key
 * stripe.webhook-secret
 */

@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {

    /**
     * Clé API secrète de Stripe (anciennement appelée secretKey, renommée pour te faire plaisir).
     * Ex : sk_test_abcdef123456
     */
    private String apiKey;

    /**
     * Secret du webhook Stripe, utilisé pour vérifier la signature des événements reçus.
     * Ex : whsec_abcdef123456
     */
    private String webhookSecret;
    
    /**
     * Devise utilisée pour les transactions Stripe.
     * Par défaut en euros (eur),
     */
    private String currency = "eur";


    // ======== GETTERS & SETTERS ========

    /**
     * Retourne la clé API Stripe.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Définit la clé API Stripe.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retourne le secret du webhook Stripe.
     */
    public String getWebhookSecret() {
        return webhookSecret;
    }

    /**
     * Définit le secret du webhook Stripe.
     */
    public void setWebhookSecret(String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}