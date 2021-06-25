package br.com.edu.cartao.carteira

import br.com.zup.AssociarCarteiraRequest
import br.com.zup.Tipo
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
class CarteiraRequest(
    @field:NotBlank @field:Email val email: String,
    @field:NotNull val tipo: TipoRequest
) {
    fun toAssociarCarteiraRequest(idCartao: String): AssociarCarteiraRequest {
        return AssociarCarteiraRequest.newBuilder().setIdCartao(idCartao).setEmail(email)
            .setTipo(tipo.atributo ?: Tipo.INVALIDO).build()
    }
}

enum class TipoRequest(val atributo: Tipo) {
    PAYPAL(Tipo.PAYPAL),
    SAMSUNG_PAY(Tipo.SAMSUNG_PAY)
}