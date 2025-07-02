package com.example.geocoding.prefecture;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.index.strtree.STRtree;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class PrefectureLookupService {

    private static class PrefecturePolygon {
        final String name;
        final Geometry geometry;
        final Envelope envelope;

        PrefecturePolygon(String name, Geometry geometry) {
            this.name = name;
            this.geometry = geometry;
            this.envelope = geometry.getEnvelopeInternal();
        }
    }

    private final STRtree index = new STRtree();
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public PrefectureLookupService(@NonNull InputStream geojson, @NonNull String attributeName, @Nullable Double bufferDistance) throws Exception {
        var reader = new InputStreamReader(
                geojson, StandardCharsets.UTF_8
        );

        var featureJson = new FeatureJSON();

        var collection = (SimpleFeatureCollection) featureJson.readFeatureCollection(reader);

        try (SimpleFeatureIterator it = collection.features()) {
            while (it.hasNext()) {
                var feature = it.next();
                var name = (String) feature.getAttribute(attributeName);
                var geom = (Geometry) feature.getDefaultGeometry();
                var geomToUse = bufferDistance != null ? geom.buffer(bufferDistance) : geom;
                PrefecturePolygon pp = new PrefecturePolygon(name, geomToUse);
                index.insert(pp.envelope, pp);
            }
            index.build();
        }
    }

    public Optional<PrefectureLookupResult> lookup(@NonNull double latitude, @NonNull double longitude) {
        var point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        List<PrefecturePolygon> candidates = index.query(point.getEnvelopeInternal());
        for (PrefecturePolygon pp : candidates) {
            if (pp.envelope.contains(point.getCoordinate()) && pp.geometry.contains(point)) {
                return Optional.of(new PrefectureLookupResult(pp.name));
            }
        }
        return Optional.empty();
    }
}
