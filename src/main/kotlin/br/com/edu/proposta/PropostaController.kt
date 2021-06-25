package br.com.edu.proposta

import br.com.zup.ConsultarPropostaRequest
import br.com.zup.PropostaGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@Controller("/proposta")
class PropostaController(@Inject val propostaClient: PropostaGrpcServiceGrpc.PropostaGrpcServiceBlockingStub) {

    @Get(uri = "/{id}")
    fun consultar(@NotNull @PathVariable("id") id: Long): HttpResponse<Any> {
        val proposta = propostaClient.consultar(ConsultarPropostaRequest.newBuilder().setId(id).build())

        return HttpResponse.ok(PropostaResponse(proposta))
    }

    @Post
    fun cadastrar(@Valid @Body propostaRequest: PropostaRequest): HttpResponse<Any> {
        val toPropostaRequest = propostaRequest.toPropostaRequest()

        val proposta = propostaClient.cadastrar(toPropostaRequest)
        val uri = UriBuilder.of("/proposta/{id}").expand(mutableMapOf(Pair("id", proposta.id)))

        return HttpResponse.created(uri)
    }

}