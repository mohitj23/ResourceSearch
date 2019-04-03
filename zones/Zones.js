const h3 = require("h3-js");
const manhattan_polygon = require("./manhattan_polygon_lat_lon.json");
const fs = require("fs");
const ZoneObj = require("./zone-model");

const hexagonsMap = {};
const zoneMap = {};


function writeToFile() {
    fs.writeFile("./manhattan_zones_lat_lon_3.json", JSON.stringify(zoneMap, undefined, 2), 'utf8', () => {
        console.log("Written to file");
    });
}

function getH3IndexFromH3Geo() {

    const hexagons = h3.polyfill(manhattan_polygon, 9, true);

    // put zones's h3Index to a Map
    hexagons.map(hex => hexagonsMap[hex] = {});

    // console.log(JSON.stringify(hexagonsMap));

    hexagons.map(hex => {
        // find the valid neighbours
        const kRingNeighbours = h3.kRingDistances(hex, 2).map(zoneList => zoneList.filter(zone => hexagonsMap[zone]));
        // console.log(kRingNeighbours);
        zoneMap[hex] = new ZoneObj(... h3.h3ToGeo(hex), hex, kRingNeighbours);
    });

    writeToFile();
}

getH3IndexFromH3Geo();