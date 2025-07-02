package com.example.geocoding.prefecture;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class PrefectureLookupServiceBenchmark {

    private record Point(double lat, double lng) {
    }

    private PrefectureLookupService service;

    private List<Point> preparedPoints;

    @Setup(Level.Trial)
    public void setupService() throws Exception {
        service = new PrefectureLookupService(new GZIPInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/data/prefecture.geojson.gz"))), "N03_001", 0.0005);
    }

    @Setup(Level.Trial)
    public void generateRandomPoint() {
        var points = new ArrayList<Point>();
        for (var i = 0; i < 100_000; i++) {
            var lat = ThreadLocalRandom.current().nextDouble(35.65, 35.75);
            var lng = ThreadLocalRandom.current().nextDouble(139.65, 139.75);
            points.add(new Point(lat, lng));
        }
        this.preparedPoints = Collections.unmodifiableList(points);
    }

    @Threads(8)
    @Benchmark
    public Optional<PrefectureLookupResult> benchmarkWithRandomPoint() {
        final var point = preparedPoints.get(ThreadLocalRandom.current().nextInt(preparedPoints.size()));
        return service.lookup(point.lat, point.lng);
    }

}
