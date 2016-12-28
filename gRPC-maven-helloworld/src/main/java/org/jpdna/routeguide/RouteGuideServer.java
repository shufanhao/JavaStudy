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

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class RouteGuideServer {
  private static final Logger logger = Logger.getLogger(RouteGuideServer.class.getName());

  /* The port on which the server should run */
  private int port = 50051;
  private Server server;
  private Collection<Feature> features;
  public RouteGuideServer() {
    this.features = initFeatures();
  }

  private void start() throws Exception {
    server = ServerBuilder.forPort(port)
        .addService(RouteGuideGrpc.bindService(new RouteGuideImpl()))
        .build()
        .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        RouteGuideServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  /*
  * init the features list
   */
  private Collection<Feature> initFeatures() {
    Collection<Feature> features = new ArrayList<Feature>(10);
    for(int i=0; i < 10; i++) {
        int latiude = 407838351 + i;
        int longtiude = -746143763 - i;
        String locationExample = "Patriots Path, Mendham, NJ 0794" + Integer.toString(i) + "USA";
        Point location = Point.newBuilder().setLatitude(latiude).setLongitude(longtiude).build();
        Feature feature = Feature.newBuilder().setName(locationExample)
                .setLocation(location).build();
        features.add(feature);
      }
    return features;
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  private class RouteGuideImpl implements RouteGuideGrpc.RouteGuide {

    @Override
    public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
      responseObserver.onNext(checkFeature(request));
      responseObserver.onCompleted();
    }

    @Override
    public void listFeatures(Rectangle request, StreamObserver<Feature> responseObserver) {

    }

    @Override
    public StreamObserver<Point> recordRoute(StreamObserver<RouteSummary> responseObserver) {
      return null;
    }

    @Override
    public StreamObserver<RouteNote> routeChat(StreamObserver<RouteNote> responseObserver) {
      return null;
    }
  }

  /**
   * Gets the feature at the given point.
   *
   * @param location the location to check.
   * @return The feature object at the point. Note that an empty name indicates no feature.
   */
  private Feature checkFeature(Point location) {
    for (Feature feature : features) {
      if (feature.getLocation().getLatitude() == location.getLatitude()
              && feature.getLocation().getLongitude() == location.getLongitude()) {
        return feature;
      }
    }

    // No feature was found, return an unnamed feature.
    return Feature.newBuilder().setName("").setLocation(location).build();
  }
  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws Exception {
    final RouteGuideServer server = new RouteGuideServer();
    server.start();
    server.blockUntilShutdown();
  }
}
