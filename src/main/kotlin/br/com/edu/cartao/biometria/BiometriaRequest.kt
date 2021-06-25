package br.com.edu.cartao.biometria

import br.com.zup.BiometriaRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

class BiometriaRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$", message = "Biometria invalido. Deve ser base64")
    val biometria: String) {

    fun toBiometriaRequest(idCartao: String): BiometriaRequest {
        return BiometriaRequest.newBuilder().setIdCartao(idCartao).setBiometria(biometria).build()
    }

}
