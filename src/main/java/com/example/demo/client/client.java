package com.example.demo.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.add.AddRequest;
import io.grpc.add.AddResponse;
import io.grpc.add.GreeterGrpc;

import java.util.concurrent.TimeUnit;
/**
 * 客户端
 */
public class client {
    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;
    private static final String host="127.0.0.1";
    private static final int ip=50051;
    public client(String host,int port){
        //usePlaintext表示明文传输，否则需要配置ssl
        //channel  表示通信通道
        channel= ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        //存根
        blockingStub=GreeterGrpc.newBlockingStub(channel);
    }
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
    public void  testResult(int num1,int num2){
        AddRequest request=AddRequest.newBuilder().setNum1(num1).setNum2(num2).build();
        AddResponse response=blockingStub.remoteAdd(request);
        System.out.println(response.getAnswer());
    }
    public static void main(String[] args) {
        client client=new client(host,ip);
        for (int i=0;i<=5;i++){
            client.testResult(i,i);
        }
    }
}

