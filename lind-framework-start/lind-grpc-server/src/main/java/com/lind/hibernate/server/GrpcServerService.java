package com.lind.hibernate.server;

import com.lind.common.GreeterGrpc;
import com.lind.common.HelloReply;
import com.lind.common.HelloRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @author lind
 * @date 2024/1/26 14:00
 * @since 1.0.0
 */
@GrpcService
public class GrpcServerService extends GreeterGrpc.GreeterImplBase {

	@Override
	public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
		HelloReply reply = HelloReply.newBuilder().setMessage("Hello ==> " + req.getName()).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

}
