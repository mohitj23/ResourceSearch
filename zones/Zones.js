const h3 = require("h3-js");
const manhattan_polygon = require("./manhattan_polygon_lat_lon.json");
const fs = require("fs");
const ZoneObj = require("./zone-model");

const hexagons = h3.polyfill(manhattan_polygon, 9, true);

const zoneMap = {};
const zones = JSON.stringify(getH3IndexFromH3Geo(hexagons));

fs.writeFile("./manhattan_zones_lat_lon_3.json", JSON.stringify(zoneMap, undefined, 2), 'utf8', () => {
    console.log(zoneMap);
});

function getH3IndexFromH3Geo(hexagons) {
    return hexagons.map(hex => {
        zoneMap[hex] = new ZoneObj(... h3.h3ToGeo(hex), hex, h3.kRingDistances(hex, 2));
    });
}