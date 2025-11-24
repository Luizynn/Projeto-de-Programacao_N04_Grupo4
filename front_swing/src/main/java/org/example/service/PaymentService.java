package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Serviço para comunicação com a API de pagamentos
 */
public class PaymentService {

    private static final String BASE_URL = "http://localhost:8080/api/payments";
    private static final String VOUCHER_BASE_URL = "http://localhost:8080/voucher";
    private static final String TICKET_BASE_URL = "http://localhost:8080/api/tickets";

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

        System.out.println("Enviando para API: " + jsonInput);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            // Tenta ler a resposta de sucesso
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
                );
                String response = br.lines().collect(Collectors.joining("\n"));
                System.out.println("Resposta de sucesso: " + response);
            } catch (Exception e) {
                System.out.println("Não foi possível ler resposta de sucesso");
            }
            return true;
        } else {
            // Tenta ler a mensagem de erro do servidor
            String errorMessage = readErrorResponse(conn);
            System.out.println("Erro da API: " + errorMessage);
            throw new Exception(errorMessage);
        }
    }

    /**
     * NOVO MÉTODO: Cria tickets para os eventos comprados
     */
    public List<Map<String, Object>> createTickets(Long clientId, String clientName,
                                                   List<Map<String, Object>> events) throws Exception {

        List<Map<String, Object>> createdTickets = new java.util.ArrayList<>();

        for (Map<String, Object> event : events) {
            try {
                Map<String, Object> ticket = createSingleTicket(
                        clientId,
                        clientName,
                        ((Number) event.get("id")).longValue(),
                        (String) event.get("name"),
                        ((Number) event.get("price")).doubleValue()
                );
                createdTickets.add(ticket);
            } catch (Exception e) {
                System.err.println("Erro ao criar ticket para evento " + event.get("name") + ": " + e.getMessage());
                // Continua criando os outros tickets mesmo se um falhar
            }
        }

        if (createdTickets.isEmpty()) {
            throw new Exception("Não foi possível criar nenhum ticket");
        }

        return createdTickets;
    }

    /**
     * NOVO MÉTODO: Cria um único ticket
     */
    private Map<String, Object> createSingleTicket(Long clientId, String clientName,
                                                   Long eventId, String eventName,
                                                   double price) throws Exception {

        URL url = new URL(TICKET_BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("clientId", clientId);
        ticketData.put("clientName", clientName);
        ticketData.put("eventId", eventId);
        ticketData.put("eventName", eventName);
        ticketData.put("price", price);

        String jsonInput = objectMapper.writeValueAsString(ticketData);
        System.out.println("Criando ticket: " + jsonInput);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            @SuppressWarnings("unchecked")
            Map<String, Object> ticket = objectMapper.readValue(
                    conn.getInputStream(),
                    Map.class
            );
            System.out.println("Ticket criado com sucesso: " + ticket.get("ticketCode"));
            return ticket;
        } else {
            String errorMessage = readErrorResponse(conn);
            throw new Exception("Erro ao criar ticket: " + errorMessage);
        }
    }

    /**
     * NOVO MÉTODO: Busca tickets de um cliente
     */
    public List<Map<String, Object>> getClientTickets(Long clientId) throws Exception {
        URL url = new URL(TICKET_BASE_URL + "/client/" + clientId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tickets = objectMapper.readValue(
                    conn.getInputStream(),
                    List.class
            );
            return tickets;
        } else {
            throw new Exception("Erro ao buscar tickets: " + responseCode);
        }
    }

    /**
     * Valida um cupom de desconto na API de Vouchers.
     */
    public double validateVoucher(String voucherCode) throws Exception {

        URL url = new URL(VOUCHER_BASE_URL + "/" + voucherCode);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = objectMapper.readValue(
                        conn.getInputStream(),
                        Map.class
                );

                if (responseMap.containsKey("data") && responseMap.get("data") instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");

                    if (dataMap.containsKey("discount") && dataMap.get("discount") instanceof Number) {
                        return ((Number) dataMap.get("discount")).doubleValue();
                    }
                }
                throw new Exception("Formato de resposta da API de voucher inválido.");

            } catch (Exception e) {
                throw new Exception("Erro ao processar resposta do voucher: " + e.getMessage());
            }

        } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST ||
                responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new Exception("cupom não existe ou foi esgotado");
        } else {
            throw new Exception("Erro no servidor de vouchers: " + responseCode);
        }
    }

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

    /**
     * Lê a resposta de erro do servidor
     */
    private String readErrorResponse(HttpURLConnection conn) {
        try {
            InputStream errorStream = conn.getErrorStream();
            if (errorStream == null) {
                return "Erro no servidor: " + conn.getResponseCode();
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(errorStream, StandardCharsets.UTF_8)
            );
            String response = br.lines().collect(Collectors.joining("\n"));

            // Tenta parsear como JSON
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> errorMap = objectMapper.readValue(response, Map.class);

                // Tenta extrair a mensagem de erro
                if (errorMap.containsKey("message")) {
                    return errorMap.get("message").toString();
                } else if (errorMap.containsKey("error")) {
                    return errorMap.get("error").toString();
                } else if (errorMap.containsKey("msg")) {
                    return errorMap.get("msg").toString();
                }
            } catch (Exception e) {
                // Se não for JSON, retorna o texto puro
            }

            return response.isEmpty() ? "Erro no servidor: " + conn.getResponseCode() : response;

        } catch (Exception e) {
            return "Erro no servidor: " + e.getMessage();
        }
    }
}