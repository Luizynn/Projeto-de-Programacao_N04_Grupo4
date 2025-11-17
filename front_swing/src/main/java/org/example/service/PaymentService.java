package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Serviço para comunicação com a API de pagamentos
 */
public class PaymentService {

    // URL base para pagamentos
    private static final String BASE_URL = "http://localhost:8080/api/payments";

    // ========================================================================
    // NOVO CAMPO ADICIONADO
    // ========================================================================
    /**
     * URL base para a API de Vouchers
     */
    private static final String VOUCHER_BASE_URL = "http://localhost:8080/api/voucher";
    // ========================================================================

    private final ObjectMapper objectMapper;

    public PaymentService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Cria um novo pagamento
     */
    public boolean createPayment(Long clientId, String paymentMethod,
                                 List<Long> eventIds, String voucherCode,
                                 String cardNumber, String expirationMonth,
                                 String expirationYear, String cvv) throws Exception {

        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Cria o JSON manualmente usando Map
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("id_client", clientId);
        paymentData.put("paymentMethod", paymentMethod);
        paymentData.put("eventId", eventIds);
        paymentData.put("voucherCode", voucherCode);
        paymentData.put("cardNumber", cardNumber);
        paymentData.put("expirationMonth", expirationMonth);
        paymentData.put("expirationYear", expirationYear);
        paymentData.put("cvv", cvv);

        String jsonInput = objectMapper.writeValueAsString(paymentData);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return true;
        } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            // Tenta ler a mensagem de erro do servidor
            String errorMessage = "Dados de pagamento inválidos";
            try {
                java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8)
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                errorMessage = response.toString();
            } catch (Exception e) {
                // Se não conseguir ler o erro, usa a mensagem padrão
            }
            throw new Exception(errorMessage);
        } else {
            System.out.println(conn.getResponseMessage());
            throw new Exception("Erro no servidor: " + responseCode);
        }
    }

    // ========================================================================
    // NOVO MÉTODO ADICIONADO
    // ========================================================================
    /**
     * Valida um cupom de desconto na API de Vouchers.
     *
     * @param voucherCode O código do cupom a ser validado.
     * @return O valor do desconto (ex: 10.0 para 10%).
     * @throws Exception Se o cupom for inválido (400/404) ou se ocorrer um erro.
     */
    public double validateVoucher(String voucherCode) throws Exception {

        URL url = new URL(VOUCHER_BASE_URL + "/" + voucherCode);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
            try {
                // Parseia a resposta: {"status": "OK", "data": {"discount": 10.00}, ...}
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = objectMapper.readValue(
                        conn.getInputStream(),
                        Map.class
                );

                // Navega até o campo "discount"
                if (responseMap.containsKey("data") && responseMap.get("data") instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");

                    if (dataMap.containsKey("discount") && dataMap.get("discount") instanceof Number) {
                        // Retorna o valor do desconto
                        return ((Number) dataMap.get("discount")).doubleValue();
                    }
                }
                // Se a estrutura do JSON for inesperada
                throw new Exception("Formato de resposta da API de voucher inválido.");

            } catch (Exception e) {
                // Erro ao ler o JSON
                throw new Exception("Erro ao processar resposta do voucher: " + e.getMessage());
            }

        } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST ||
                responseCode == HttpURLConnection.HTTP_NOT_FOUND) { // 400 ou 404

            // Mensagem de erro exata que você solicitou
            throw new Exception("cupom não existe ou foi esgotado");

        } else {
            // Outros erros de servidor (500, 503, etc.)
            throw new Exception("Erro no servidor de vouchers: " + responseCode);
        }
    }
    // ========================================================================

    /**
     * Busca todos os pagamentos
     */
    public List<Map<String, Object>> findAllPayments() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> payments = objectMapper.readValue(
                    conn.getInputStream(),
                    List.class
            );
            return payments;
        } else {
            throw new Exception("Erro ao buscar pagamentos: " + responseCode);
        }
    }

    /**
     * Busca pagamentos de um cliente específico
     */
    public List<Map<String, Object>> findPaymentsByClient(Long clientId) throws Exception {
        URL url = new URL(BASE_URL + "/client/" + clientId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> payments = objectMapper.readValue(
                    conn.getInputStream(),
                    List.class
            );
            return payments;
        } else {
            throw new Exception("Erro ao buscar pagamentos do cliente: " + responseCode);
        }
    }

    /**
     * Edita um pagamento existente
     */
    public Map<String, Object> editPayment(Long paymentId, String status,
                                           String paymentMethod, Double totalAmount)
            throws Exception {

        URL url = new URL(BASE_URL + "/" + paymentId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PATCH");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Cria o JSON manualmente
        Map<String, Object> editData = new HashMap<>();
        if (status != null) editData.put("status", status);
        if (paymentMethod != null) editData.put("paymentMethod", paymentMethod);
        if (totalAmount != null) editData.put("totalAmount", totalAmount);

        String jsonInput = objectMapper.writeValueAsString(editData);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            @SuppressWarnings("unchecked")
            Map<String, Object> payment = objectMapper.readValue(
                    conn.getInputStream(),
                    Map.class
            );
            return payment;
        } else {
            throw new Exception("Erro ao editar pagamento: " + responseCode);
        }
    }
}