
// import olFeature from 'ol/Feature';
import * as olStyle from 'ol/style';
// import * as olGeom from 'ol/geom';

const image = new olStyle.Circle({
	radius: 5,
	fill: null,
	stroke: new olStyle.Stroke({ color: 'red', width: 1 })
});

export default {
	"Point": new olStyle.Style({
		image: image
	}),
	"LineString": new olStyle.Style({
		stroke: new olStyle.Stroke({
			color: "green",
			width: 10
		})
	}),
	"MultiLineString": new olStyle.Style({
		stroke: new olStyle.Stroke({
			color: "green",
			width: 1
		})
	}),
	"MultiPoint": new olStyle.Style({
		image: image
	}),
	"MultiPolygon": new olStyle.Style({
		stroke: new olStyle.Stroke({
			color: "yellow",
			width: 1
		}),
		fill: new olStyle.Fill({
			color: "rgba(255, 255, 0, 0.1)"
		})
	}),
	"Polygon": new olStyle.Style({
		stroke: new olStyle.Stroke({
			color: "blue",
			lineDash: [4],
			width: 3
		}),
		fill: new olStyle.Fill({
			color: "rgba(0, 0, 255, 0.1)"
		})
	}),
	"GeometryCollection": new olStyle.Style({
		stroke: new olStyle.Stroke({
			color: "magenta",
			width: 2
		}),
		fill: new olStyle.Fill({
			color: "magenta"
		}),
		image: new olStyle.Circle({
			radius: 10,
			fill: null,
			stroke: new olStyle.Stroke({
				color: "magenta"
			})
		})
	}),
	"Circle": new olStyle.Style({
		stroke: new olStyle.Stroke({
			color: "red",
			width: 2
		}),
		fill: new olStyle.Fill({
			color: "rgba(255,0,0,0.2)"
		})
	})
}
