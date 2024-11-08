package App.Domain;

public record ClienteResponse(

        String nome,
        String documento,
        String email,
        String saldo
) {
}
