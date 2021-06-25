package br.com.edu.util

import br.com.zup.BiometriaGrpcServiceGrpc
import br.com.zup.CartaoGrpcServiceGrpc
import br.com.zup.PropostaGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
open class GrpcClientFactory(@GrpcChannel("proposta") val channel: ManagedChannel) {

    @Singleton
    fun propostaServiceClientStub() = PropostaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun biomatriaServiceClientStub() = BiometriaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun cartaoServiceClientStub() = CartaoGrpcServiceGrpc.newBlockingStub(channel)

}