const method = zoneModel.prototype;

//Constructor for zoneModel
function zoneModel(lat, long, h3Index) {
    this.lat = lat;
    this.long = long;
    this.h3Index = h3Index;
}

method.h3Index = function (h3Index) {
    this.h3Index = h3Index
};

method.addConstruct = function (constructObj) {
    this.change = constructObj
};

module.exports = zoneModel;