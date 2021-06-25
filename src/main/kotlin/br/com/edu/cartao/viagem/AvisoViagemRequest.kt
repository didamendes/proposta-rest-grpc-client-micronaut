package br.com.edu.cartao.viagem

import br.com.zup.ViagemRequest
import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.NotBlank

@Introspected
class AvisoViagemRequest(
    @field:FutureOrPresent val validoAte: LocalDate,
    @field:NotBlank val destino: String
) {
    fun toViagemRequest(idCartao: String): ViagemRequest? {
        return ViagemRequest.newBuilder().setIdCartao(idCartao).setValidoAte(validoAte.toString()).setDestino(destino)
            .build()
    }
}
