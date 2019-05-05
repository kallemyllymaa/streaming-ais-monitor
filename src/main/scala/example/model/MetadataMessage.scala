package example.model

/** A metadata-message
  *
  */
case class MetadataMessage(
    mmsi: Long,
    name: String,
    shipType: Int,
    referencePointA: Int,
    referencePointB: Int,
    referencePointC: Int,
    referencePointD: Int,
    posType: Int,
    draught: Int,
    imo: Long,
    callSign: String,
    eta: Long,
    timestamp: Long,
    destination: String
)
