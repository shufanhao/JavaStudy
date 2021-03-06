/*
 * Copyright 2016, Google Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 *    * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jpdna.errorhandling;

import com.google.common.base.Verify;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Uninterruptibles;

import org.jpdna.grpchello.GreeterGrpc;
import org.jpdna.grpchello.HelloRequest;
import org.jpdna.grpchello.HelloResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.grpc.CallOptions;
import io.grpc.ClientCall;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/**
 * Shows how to extract error information from a server response.
 */
public class ErrorHandlingClient {
  public static void main(String [] args) throws Exception {
    new ErrorHandlingClient().run();
  }

  private ManagedChannel channel;

  void run() throws Exception {
    // Port 0 means that the operating system will pick an available port to use.
    channel =
        ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext(true).build();

    blockingCall();
    futureCallDirect();
    futureCallCallback();
    asyncCall();
    advancedAsyncCall();

    channel.shutdown();
    channel.awaitTermination(1, TimeUnit.SECONDS);
  }

  void blockingCall() {
    GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
    try {
      stub.sayHello(HelloRequest.newBuilder().setName("Bart").build());
    } catch (Exception e) {
      Status status = Status.fromThrowable(e);
      Verify.verify(status.getCode() == Status.Code.INTERNAL);
      Verify.verify(status.getDescription().contains("Eggplant"));
      // Cause is not transmitted over the wire.
    }
  }

  void futureCallDirect() {
    GreeterGrpc.GreeterFutureStub stub = GreeterGrpc.newFutureStub(channel);
    ListenableFuture<HelloResponse> response =
        stub.sayHello(HelloRequest.newBuilder().setName("Lisa").build());

    try {
      response.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      Status status = Status.fromThrowable(e.getCause());
      Verify.verify(status.getCode() == Status.Code.INTERNAL);
      Verify.verify(status.getDescription().contains("Xerxes"));
      // Cause is not transmitted over the wire.
    }
  }

  void futureCallCallback() {
    GreeterGrpc.GreeterFutureStub stub = GreeterGrpc.newFutureStub(channel);
    ListenableFuture<HelloResponse> response =
        stub.sayHello(HelloRequest.newBuilder().setName("Maggie").build());

    final CountDownLatch latch = new CountDownLatch(1);

    Futures.addCallback(response, new FutureCallback<HelloResponse>() {
      @Override
      public void onSuccess(@Nullable HelloResponse result) {
        // Won't be called, since the server in this example always fails.
      }

      @Override
      public void onFailure(Throwable t) {
        Status status = Status.fromThrowable(t);
        Verify.verify(status.getCode() == Status.Code.INTERNAL);
        Verify.verify(status.getDescription().contains("Crybaby"));
        // Cause is not transmitted over the wire..
        latch.countDown();
      }
    });

    if (!Uninterruptibles.awaitUninterruptibly(latch, 1, TimeUnit.SECONDS)) {
      throw new RuntimeException("timeout!");
    }
  }

  void asyncCall() {
    GreeterGrpc.GreeterStub stub = GreeterGrpc.newStub(channel);
    HelloRequest request = HelloRequest.newBuilder().setName("Homer").build();
    final CountDownLatch latch = new CountDownLatch(1);
    StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {

      @Override
      public void onNext(HelloResponse value) {
        // Won't be called.
      }

      @Override
      public void onError(Throwable t) {
        Status status = Status.fromThrowable(t);
        Verify.verify(status.getCode() == Status.Code.INTERNAL);
        Verify.verify(status.getDescription().contains("Overbite"));
        // Cause is not transmitted over the wire..
        latch.countDown();
      }

      @Override
      public void onCompleted() {
        // Won't be called, since the server in this example always fails.
      }
    };
    stub.sayHello(request, responseObserver);

    if (!Uninterruptibles.awaitUninterruptibly(latch, 1, TimeUnit.SECONDS)) {
      throw new RuntimeException("timeout!");
    }
  }


  /**
   * This is more advanced and does not make use of the stub.  You should not normally need to do
   * this, but here is how you would.
   */
  void advancedAsyncCall() {
    ClientCall<HelloRequest, HelloResponse> call =
        channel.newCall(GreeterGrpc.METHOD_SAY_HELLO, CallOptions.DEFAULT);

    final CountDownLatch latch = new CountDownLatch(1);

    call.start(new ClientCall.Listener<HelloResponse>() {

      @Override
      public void onClose(Status status, Metadata trailers) {
        Verify.verify(status.getCode() == Status.Code.INTERNAL);
        Verify.verify(status.getDescription().contains("Narwhal"));
        // Cause is not transmitted over the wire.
        latch.countDown();
      }
    }, new Metadata());

    call.sendMessage(HelloRequest.newBuilder().setName("Marge").build());
    call.halfClose();

    if (!Uninterruptibles.awaitUninterruptibly(latch, 1, TimeUnit.SECONDS)) {
      throw new RuntimeException("timeout!");
    }
  }
}

