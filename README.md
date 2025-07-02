# Geocoding API
緯度経度から、日本の都道府県を判定するAPIです。

## How to use

```
http://localhost:8080/prefecture?lng=139.3868279&lat=35.680772

→ {"name":"東京都"}  
```

## Map data

[国土数値情報ダウンロードサイト](https://nlftp.mlit.go.jp/cgi-bin/isj/dls/_choose_method.cgi)
からダウンロードした N03-20240101_prefecture.geojson を加工して利用しています。

```shell
ogr2ogr -f GeoJSON \
  -simplify 0.005 \
  -makevalid \
  prefecture.geojson \
  N03-20240101_prefecture.geojson
```

## Benchmark

```
Intel(R) Core(TM) i5-1038NG7 CPU @ 2.00GHz
Memory: 16GB
Java: openjdk 24.0.1 2025-04-15

Benchmark                                                   Mode  Cnt      Score      Error  Units
PrefectureLookupServiceBenchmark.benchmarkWithRandomPoint  thrpt    5  94869.466 ± 5759.481  ops/s
```