package com.pagamentos.projeto_programacao.payment;

import java.time.YearMonth;

public class CardValidator {


    public static class CardValidationException extends Exception {
        public CardValidationException(String message) {
            super(message);
        }
    }

    public static class InvalidNumberException extends CardValidationException {
        public InvalidNumberException(String message) {
            super(message);
        }
    }

    public static class InvalidExpirationException extends CardValidationException {
        public InvalidExpirationException(String message) {
            super(message);
        }
    }

    public static class InvalidCVVException extends CardValidationException {
        public InvalidCVVException(String message) {
            super(message);
        }
    }


    public static class CardValidationResult {
        private final boolean valid;
        private final String message;
        private final String brand;

        public CardValidationResult(boolean valid, String message, String brand) {
            this.valid = valid;
            this.message = message;
            this.brand = brand;
        }

        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public String getBrand() { return brand; }

        @Override
        public String toString() {
            return "Válido: " + valid + " | Mensagem: " + message + " | Bandeira: " + brand;
        }
    }

    // principal

    public static CardValidationResult validate(String cardNumber, String expirationMonth, String expirationYear, String cvv)
            throws CardValidationException {

        if (cardNumber == null || cardNumber.isBlank()) {
            throw new InvalidNumberException("Número do cartão não pode estar vazio");
        }

        String sanitized = cardNumber.replaceAll("[^\\d]", "");

        if (sanitized.length() < 13 || sanitized.length() > 19) {
            throw new InvalidNumberException("O número do cartão deve ter entre 13 e 19 dígitos");
        }

        if (!passesLuhnCheck(sanitized)) {
            throw new InvalidNumberException("Falha no dígito verificador (algoritmo de Luhn)");
        }

        String brand = detectCardBrand(sanitized);

        if (!isValidExpirationDate(expirationMonth, expirationYear)) {
            throw new InvalidExpirationException("Data de validade inválida ou expirada");
        }

        if (!isValidCVV(cvv, brand)) {
            throw new InvalidCVVException("Código de segurança (CVV) inválido para a bandeira " + brand);
        }

        return new CardValidationResult(true, "Cartão válido", brand);
    }


    public static CardValidationResult tryValidate(String cardNumber, String month, String year, String cvv) {
        try {
            return validate(cardNumber, month, year, cvv);
        } catch (CardValidationException e) {
            String brand = detectCardBrand(cardNumber);
            return new CardValidationResult(false, e.getMessage(), brand);
        }
    }


    private static boolean passesLuhnCheck(String number) {
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return sum % 10 == 0;
    }

    private static boolean isValidExpirationDate(String month, String year) {
        try {
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);

            if (m < 1 || m > 12) return false;

            YearMonth expiration = YearMonth.of(y, m);
            YearMonth now = YearMonth.now();

            return !expiration.isBefore(now);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isValidCVV(String cvv, String brand) {
        if (cvv == null || !cvv.matches("\\d+")) return false;

        int length = cvv.length();
        return switch (brand) {
            case "American Express" -> length == 4;
            case "Visa", "MasterCard" -> length == 3;
            default -> (length == 3 || length == 4);
        };
    }

    public static String detectCardBrand(String cardNumber) {
        if (cardNumber == null) return "Desconhecido";

        String sanitized = cardNumber.replaceAll("[^\\d]", "");

        if (sanitized.matches("^4[0-9]{12}(?:[0-9]{3})?$")) return "Visa";

        if (sanitized.matches("^5[1-5][0-9]{14}$")) return "MasterCard";

        if (sanitized.matches("^3[47][0-9]{13}$")) return "American Express";

        return "Desconhecido";
    }

    public static String formatCardNumber(String cardNumber) {
        if (cardNumber == null) return "";
        String sanitized = cardNumber.replaceAll("[^\\d]", "");
        String brand = detectCardBrand(sanitized);

        if (brand.equals("American Express")) {
            return sanitized.replaceAll("(\\d{4})(\\d{6})(\\d+)", "$1 $2 $3");
        } else {
            return sanitized.replaceAll("(.{4})", "$1 ").trim();
        }
    }
}


/*
Exemplos de uso:
    CarCardValidationResult visaValido = CardValidator.tryValidate(
            "4539970000000000", "12", "2030", "123"
        );
        System.out.println("Visa Válido: " + visaValido);


        CardValidationResult amexValido = CardValidator.tryValidate(
            "371449635398431", "10", "2029", "9876"
        );
        System.out.println("Amex Válido: " + amexValido);


        CardValidationResult falhaLuhn = CardValidator.tryValidate(
            "4539970000000001", "12", "2030", "123" // Mudei o último dígito
        );
        System.out.println("Falha Luhn:  " + falhaLuhn);


        CardValidationResult expirado = CardValidator.tryValidate(
            "4539970000000000", "05", "2024", "123" // Estamos em 2025
        );
        System.out.println("Expirado:    " + expirado);


        CardValidationResult nulo = CardValidator.tryValidate(
            null, "12", "2030", "123"
        );
        System.out.println("Nulo:        " + nulo);


        CardValidationResult sujo = CardValidator.tryValidate(
            " 4539-9700-0000-0000 ", "12", "2030", "123"
        );
        System.out.println("Input 'Sujo':" + sujo);


        System.out.println("\n--- 2. Testando validate (com exceções) ---");

        try {

            CardValidationResult masterValido = CardValidator.validate(
                "5555444433332222", "08", "2028", "567"
            );
            System.out.println("Master Válido: " + masterValido);


            System.out.println("\nTestando erro de CVV no Amex (espera exceção)...");
            CardValidator.validate(
                "371449635398431", "10", "2029", "123" // CVV de 3 dígitos (inválido p/ Amex)
            );
            System.out.println("ERRO: Esta linha não deveria ser impressa.");

        } catch (CardValidator.InvalidCVVException e) {
            System.out.println("Exceção Capturada (CVV): " + e.getMessage());
        } catch (CardValidator.CardValidationException e) {
            // Pega qualquer outra exceção (Número, Data, etc.)
            System.out.println("Exceção Capturada (Geral): " + e.getMessage());
        }


        System.out.println("\n--- 3. Testando Formatação ---");

        String amexSujo = "3714 496353 98431";
        String visaSujo = "4539-9700-0000-0000";
        String masterSujo = "5555444433332222";

        System.out.println("Amex Formatado:   " + CardValidator.formatCardNumber(amexSujo));
        System.out.println("Visa Formatado:   " + CardValidator.formatCardNumber(visaSujo));
        System.out.println("Master Formatado: " + CardValidator.formatCardNumber(masterSujo));
    }
}
*/