package com.example.demo.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.justtest.GreeterGrpc;
import io.grpc.justtest.TestRequest;
import io.grpc.justtest.TestResponse;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * 服务端
 */
public class server {
    //定义端口
    private final int port = 50051;
    //服务
    private Server server;

    //启动服务,并且接受请求
    private void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build().start();
        System.out.println("服务开始启动-------");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("------shutting down gRPC server since JVM is shutting down-------");
                server.this.stop();
                System.err.println("------server shut down------");
            }
        });
    }

    //stop服务
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
    //server阻塞到程序退出
    private void  blockUntilShutdown() throws InterruptedException {
        if (server!=null){
            server.awaitTermination();
        }
    }

    //实现服务接口的类
    private class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void testSomeThing(TestRequest request, StreamObserver<TestResponse> responseObserver) {
            TestResponse build = TestResponse.newBuilder().setMessage(request.getNum1() + request.getNum2()).build();
            //onNext()方法向客户端返回结果
            System.out.println("服务端已经接受");
            responseObserver.onNext(build);
            //告诉客户端这次调用已经完成
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final  server server=new server();
        server.start();
        server.blockUntilShutdown();
    }
}

