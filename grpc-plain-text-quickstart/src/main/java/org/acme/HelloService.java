package org.acme;

import io.quarkus.example.Greeter;
import io.quarkus.example.HelloReply;
import io.quarkus.example.HelloRequest;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService // 実装をBeanとして公開します。
public class HelloService implements Greeter { // 生成されたサービスインターフェースを実装

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) { //　サービスで定義されたメソッドを使用
        return Uni.createFrom().item(() ->
                HelloReply.newBuilder().setMessage("Hello " + request.getName()).build()
        );
    }
}