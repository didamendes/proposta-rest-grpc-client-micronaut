package br.com.edu.proposta

import br.com.zup.PropostaRequest
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class PropostaRequest(
    @field:NotBlank val documento: String,
    @field:NotBlank @field:Email val email: String,
    @field:NotBlank val nome: String,
    @field:NotBlank val endereco: String,
    @field:NotNull val salario: BigDecimal
) {
    fun toPropostaRequest(): PropostaRequest {
        return PropostaRequest.newBuilder().setDocumento(documento).setEmail(email).setNome(nome).setEndereco(endereco).setSalario(salario.toString()).build()
    }
}
