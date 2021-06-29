package br.com.edu.proposta

import br.com.edu.util.GrpcClientFactory
import br.com.zup.ConsultarPropostaResponse
import br.com.zup.PropostaGrpcServiceGrpc
import br.com.zup.PropostaResponse
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class PropostaControllerTest {

    @Inject
    lateinit var client: PropostaGrpcServiceGrpc.PropostaGrpcServiceBlockingStub

    @Inject
    @field:Client(value = "/")
    lateinit var clientHttp: HttpClient

    @Test
    internal fun `cadastrar uma nova proposta`() {

        Mockito.`when`(client.cadastrar(Mockito.any())).thenReturn(PropostaResponse.newBuilder().setId("1").build())

        val propostaRequest = PropostaRequest(
            documento = "46121663456",
            email = "dove@zup.com.br",
            nome = "Dove",
            endereco = "Rua Joel",
            salario = BigDecimal(50000)
        )

        val request = HttpRequest.POST("/proposta", propostaRequest)

        val response = clientHttp.toBlocking().exchange(request, br.com.zup.PropostaRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Test
    internal fun `deve buscar proposta pelo id informado`() {
        val id = 1L
        val consultarPropostaResponseMock = consultarPropostaResponseMock()

        Mockito.`when`(client.consultar(Mockito.any())).thenReturn(consultarPropostaResponseMock)

        val response = clientHttp.toBlocking().exchange("/proposta/${id}", Any::class.java)
        assertEquals(HttpStatus.OK, response.status)
    }

    /**
     * Mock classes
     *
     */
    fun consultarPropostaResponseMock(): ConsultarPropostaResponse? {
        return ConsultarPropostaResponse.newBuilder().setId(1).setDocumento("46121663456").setEmail("dove@zup.com.br")
            .setNome("Dove").setEndereco("Rua Joel").setSalario("5000").build()
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class GrpcFactory {
        @Singleton
        fun stub() = Mockito.mock(PropostaGrpcServiceGrpc.PropostaGrpcServiceBlockingStub::class.java)
    }

}