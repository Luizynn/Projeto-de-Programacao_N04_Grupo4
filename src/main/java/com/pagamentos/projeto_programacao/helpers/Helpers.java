package com.pagamentos.projeto_programacao.helpers;


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

}
