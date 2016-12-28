/*
 * Copyright 2015, Google Inc. All rights reserved.
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

package org.jpdna.routeguide;

import com.google.api.client.repackaged.com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * A simple client that requests a greeting from the {@link RouteGuideServer}.
 */
public class RouteGuideClient {
  private static final Logger logger = Logger.getLogger(RouteGuideClient.class.getName());

  private final ManagedChannel channel;
  private final RouteGuideGrpc.RouteGuideBlockingStub blockingStub;
  private final RouteGuideGrpc.RouteGuideStub asyncStub;
  private TestHelper testHelper;
  private Random random = new Random();

  /** Construct client connecting to RouteGuide server at {@code host:port}. */
  public RouteGuideClient(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext(true)
        .build();
    blockingStub = RouteGuideGrpc.newBlockingStub(channel);
    asyncStub = RouteGuideGrpc.newStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /*
   * Blocking unary call example, Calls getFeature and prints the response
   */
  public void getFeature(int lat, int lon) {
      logger.info("*** GetFeature: lat=" + lat + " lon= " + lon);
      Point request = Point.newBuilder().setLatitude(lat).setLongitude(lon).build();
      Feature feature = blockingStub.getFeature(request);
      logger.info("Get the response Feature name " + feature.getName() +
              " Latitude " + RouteGuideUtil.getLatitude(feature.getLocation()) +
              " Longitude " + RouteGuideUtil.getLongitude(feature.getLocation()));
  }


  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting.
   */
  public static void main(String[] args) throws Exception {
    RouteGuideClient client = new RouteGuideClient("localhost", 50051);
    try {
      /* Access a service running on the local machine on port 50051 */
      client.getFeature(407838351, -746143763);
    } finally {
      client.shutdown();
    }
  }

    /**
     * Only used for unit test, as we do not want to introduce randomness in unit test.
     */
    @VisibleForTesting
    void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Only used for helping unit test.
     */
    @VisibleForTesting
    interface TestHelper {
        /**
         * Used for verify/inspect message received from server.
         */
        void onMessage(Message message);

        /**
         * Used for verify/inspect error received from server.
         */
        void onRpcError(Throwable exception);
    }

    @VisibleForTesting
    void setTestHelper(TestHelper testHelper) {
        this.testHelper = testHelper;
    }
}
