package example.model

/**
 *
 */
case class LocationMessage(
    mmsi: Long,
    geometry: LocationMessageGeometry,
    properties: LocationMessageProperties
)

/**
 * 
 */
case class LocationMessageGeometry(coordinates: Array[Double])

/**
 * 
 */
case class LocationMessageProperties(
    sog: Double,
    posAcc: Boolean,
    heading: Int,
    timestampExternal: Long
)