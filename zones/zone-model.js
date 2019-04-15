const method = zoneModel.prototype;

//Constructor for zoneModel
function zoneModel(lat, long1, h3Index, kRingNeighbors) {
    this.lat = lat;
    this.long1 = long1;
    this.h3Index = h3Index;
    this.kRingNeighbors = kRingNeighbors;
}

method.h3Index = function (h3Index) {
    this.h3Index = h3Index
};

module.exports = zoneModel;