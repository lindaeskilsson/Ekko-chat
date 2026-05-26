package com.ekko.user.grpc;

import com.ekko.user.domain.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    @Override
    public void getUser(UserProto.GetUserRequest request,
                        StreamObserver<UserProto.GetUserResponse> responseObserver) {
        userRepository.findById(request.getUserId())
                .ifPresentOrElse(
                        user -> {
                            var response = UserProto.GetUserResponse.newBuilder()
                                    .setId(user.getId())
                                    .setUsername(user.getUsername())
                                    .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                io.grpc.Status.NOT_FOUND
                                        .withDescription("User not found")
                                        .asRuntimeException()
                        )
                );
    }
}