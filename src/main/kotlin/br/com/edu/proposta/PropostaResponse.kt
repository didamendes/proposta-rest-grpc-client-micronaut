package br.com.edu.proposta

import br.com.zup.ConsultarPropostaResponse
import java.math.BigDecimal

class PropostaResponse(response: ConsultarPropostaResponse) {
    val id = response.id
    val documento = response.documento
    val email = response.email
    val nome = response.nome
    val endereco = response.endereco
    val salario = BigDecimal(response.salario)
    val status = response.status
}