package dev.java.clean_architecture.domain.valueobject;

public record Name(

        String value

) {

    public Name {

        if(value == null || value.isEmpty()){
            throw new IllegalArgumentException("Nome do produto inválido");
        }
        if(value.length() < 2 || value.length() > 100){
            throw new IllegalArgumentException("O produto deve ter entre 2-100 caracteres");
        }

    }

}
