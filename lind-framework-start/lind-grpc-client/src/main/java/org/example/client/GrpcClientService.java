package org.example.client;

import com.lind.common.GreeterGrpc;
import com.lind.common.HelloReply;
import com.lind.common.HelloRequest;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * @author Michael (yidongnan@gmail.com)
 * @since 2016/11/8
 */
@Service
public class GrpcClientService {

	@GrpcClient("local-grpc-server")
	private GreeterGrpc.GreeterBlockingStub simpleStub;

	public String sendMessage(final String name) {
		try {
			final HelloReply response = this.simpleStub.sayHello(HelloRequest.newBuilder().setName(name).build());
			return response.getMessage();
		}
		catch (final StatusRuntimeException e) {
			return "FAILED with " + e.getStatus().getCode().name();
		}
	}

}
