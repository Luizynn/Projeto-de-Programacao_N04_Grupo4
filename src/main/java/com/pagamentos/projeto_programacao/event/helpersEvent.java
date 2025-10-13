package com.pagamentos.projeto_programacao.event;

public class helpersEvent {
    static boolean checkName(String name){
        if(name.trim().length() > 155 || name.isEmpty()){
            System.out.println("Quantidade de caracteres excedente, tente novamente");
            return false;
        }

        return true;
    }
    static String formatName(String name){
        String[] newName = name.split(" ") ;
        String formattedName = "";
        for(String names : newName) {
            String capitalizedName = names.substring(0,1).toUpperCase() + names.substring(1);
            formattedName += capitalizedName;
        }
        return formattedName;

    }

}

