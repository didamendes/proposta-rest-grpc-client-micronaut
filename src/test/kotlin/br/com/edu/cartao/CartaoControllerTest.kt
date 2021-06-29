package br.com.edu.cartao

import br.com.edu.cartao.biometria.BiometriaRequest
import br.com.edu.cartao.carteira.CarteiraRequest
import br.com.edu.cartao.carteira.TipoRequest
import br.com.edu.cartao.viagem.AvisoViagemRequest
import br.com.edu.util.GrpcClientFactory
import br.com.zup.*
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CartaoControllerTest {

    @Inject
    lateinit var clientCartao: CartaoGrpcServiceGrpc.CartaoGrpcServiceBlockingStub

    @Inject
    lateinit var clientBiometria: BiometriaGrpcServiceGrpc.BiometriaGrpcServiceBlockingStub

    @Inject
    @field:Client(value = "/")
    lateinit var clientHttp: HttpClient

    @Test
    internal fun `deve cadastrar uma biometria`() {
        val idCartao = "3300-1227-9032-8653"

        Mockito.`when`(clientBiometria.cadastrar(Mockito.any()))
            .thenReturn(BiometriaResponse.newBuilder().setId(1).build())

        val biometriaRequest = BiometriaRequest(biometria = "dGVzdGU=")

        val request = HttpRequest.POST("/cartaoes/biometria/${idCartao}", biometriaRequest)
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Test
    internal fun `deve bloquear um cartao`() {
        val idCartao = "3300-1227-9032-8653"

        Mockito.`when`(clientCartao.bloquear(Mockito.any())).thenReturn(BloqueioResponse.newBuilder().setId(1).build())

        val request = HttpRequest.POST("/cartaoes/bloqueio/${idCartao}", Any::class.java)
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Test
    internal fun `deve cadastrar um aviso de viagem`() {
        val idCartao = "3300-1227-9032-8653"

        Mockito.`when`(clientCartao.avisoViagem(Mockito.any())).thenReturn(ViagemResponse.newBuilder().setId(1).build())

        val avisoViagemRequest = AvisoViagemRequest(
            validoAte = LocalDate.parse("2021-12-12", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            destino = "Londres"
        )

        val request = HttpRequest.POST("/cartaoes/aviso-viagem/${idCartao}", avisoViagemRequest)
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Test
    internal fun `deve associar uma cartao ao carteira digital`() {
        val idCartao = "3300-1227-9032-8653"

        Mockito.`when`(clientCartao.associarCarteira(Mockito.any()))
            .thenReturn(AssociarCarteiraResponse.newBuilder().setId(1).build())

        val carteiraRequest = CarteiraRequest(email = "dove@zup.com.br", tipo = TipoRequest.PAYPAL)

        val request = HttpRequest.POST("/cartaoes/associar/${idCartao}", carteiraRequest)
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class GrpcFacotry {

        @Singleton
        fun stubCartao() = Mockito.mock(CartaoGrpcServiceGrpc.CartaoGrpcServiceBlockingStub::class.java)

        @Singleton
        fun stubBiometria() = Mockito.mock(BiometriaGrpcServiceGrpc.BiometriaGrpcServiceBlockingStub::class.java)
    }

}