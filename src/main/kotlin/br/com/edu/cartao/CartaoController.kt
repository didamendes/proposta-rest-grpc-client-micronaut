package br.com.edu.cartao

import br.com.edu.cartao.biometria.BiometriaRequest
import br.com.edu.cartao.carteira.CarteiraRequest
import br.com.edu.cartao.viagem.AvisoViagemRequest
import br.com.zup.BiometriaGrpcServiceGrpc
import br.com.zup.BloqueioRequest
import br.com.zup.CartaoGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/cartaoes")
class CartaoController(
    @Inject val biometriaClient: BiometriaGrpcServiceGrpc.BiometriaGrpcServiceBlockingStub,
    @Inject val cartaoClient: CartaoGrpcServiceGrpc.CartaoGrpcServiceBlockingStub
) {

    /**
     * Cadastrar Biometria
     *
     */
    @Post(uri = "/biometria/{idCartao}")
    fun cadastrarBiometria(
        @PathVariable("idCartao") idCartao: String,
        @Body biometriaRequesst: BiometriaRequest
    ): HttpResponse<Any> {
        val toBiometriaRequest = biometriaRequesst.toBiometriaRequest(idCartao)
        val biometria = biometriaClient.cadastrar(toBiometriaRequest)

        val uri = UriBuilder.of("/cartoes/biometria/{id}").expand(mutableMapOf(Pair("id", biometria.id)))
        return HttpResponse.created(uri)
    }

    /**
     * Bloqueio do Cartao
     *
     */
    @Post(uri = "/bloqueio/{idCartao}")
    fun bloquearCartao(@PathVariable("idCartao") idCartao: String): HttpResponse<Any> {
        val bloqueio = cartaoClient.bloquear(BloqueioRequest.newBuilder().setIdCartao(idCartao).build())

        val uri = UriBuilder.of("/cartoes/bloqueio/{id}").expand(mutableMapOf(Pair("id", bloqueio.id)))
        return HttpResponse.created(uri)
    }

    /**
     * Avisar viagem
     *
     */
    @Post(uri = "/aviso-viagem/{idCartao}")
    fun avisarViagem(
        @PathVariable("idCartao") idCartao: String,
        @Valid @Body viagemRequest: AvisoViagemRequest
    ): HttpResponse<Any> {
        val toViagemRequest = viagemRequest.toViagemRequest(idCartao)
        val avisoViagem = cartaoClient.avisoViagem(toViagemRequest)

        val uri = UriBuilder.of("/cartoes/aviso-viagem/{id}").expand(mutableMapOf(Pair("id", avisoViagem.id)))
        return HttpResponse.created(uri)
    }

    /**
     * Associar Carteira
     *
     */
    @Post(uri = "/associar/{idCartao}")
    fun associarCartao(
        @PathVariable("idCartao") idCartao: String,
        @Valid @Body cateiraRequest: CarteiraRequest
    ): HttpResponse<Any> {
        val toAssociarCarteiraRequest = cateiraRequest.toAssociarCarteiraRequest(idCartao)

        val associarCarteira = cartaoClient.associarCarteira(toAssociarCarteiraRequest)

        val uri = UriBuilder.of("/cartoes/associar/{id}").expand(mutableMapOf(Pair("id", associarCarteira.id)))
        return HttpResponse.created(uri)
    }

}