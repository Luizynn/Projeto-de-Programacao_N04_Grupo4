package com.pagamentos.projeto_programacao.helpers;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Helpers {
    public static boolean checkCpf  (String cpf) {
        if(cpf.length() != 11) {
            return false;
        }

        int total_1 = 0;
        int index = 10;

        for (String num : cpf.substring(0,9).split("")) {
            total_1 += Integer.parseInt(num) * index;
            System.out.println(Integer.parseInt(num));
            index--;
        }

        System.out.println(total_1);

        int rest_1 = total_1 % 11;

        rest_1 = 11 - rest_1;

        rest_1 = rest_1 > 9 ? 0 : rest_1;

        if(Character.getNumericValue(cpf.charAt(9)) != rest_1) {
            return false;
        }

        index = 11;
        total_1 = 0;

        for (String num : cpf.substring(0,10).split("")) {
            total_1 += Integer.parseInt(num) * index;
            System.out.println(Integer.parseInt(num));
            index--;
        }

        rest_1 = total_1 % 11;

        rest_1 = 11 - rest_1;

        rest_1 = rest_1 > 9 ? 0 : rest_1;

        if(Character.getNumericValue(cpf.charAt(10)) != rest_1) {
            return false;
        }

        return true;
    }

    public static boolean checkName(String name){
        if(name.trim().length() > 155 || name.isEmpty()){
            System.out.println("Quantidade de caracteres excedente, tente novamente");
            return false;
        }

        return true;
    }
    
    public static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String getTodayDate(){
        LocalDate today = LocalDate.now();
        return today.format(FORMATADOR_DATA);
    }

    public static final DateTimeFormatter FORMATTER_HOUR = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String getCurrentHour(){
        LocalTime now = LocalTime.now();
        return now.format(FORMATTER_HOUR);
    }
}
