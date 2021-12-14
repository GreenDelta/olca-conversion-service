package app.model

import org.openlca.io.maps.FlowMap
import org.openlca.io.maps.FlowMapEntry

class ConversionSetup {
    var refSystem = ""
    var url = ""
    var sourceFormat = ""
    var targetFormat = ""
    var flowMapping: List<FlowMapEntry>? = null

    fun flowMap(): FlowMap? {
        if (flowMapping == null)
            return null
        val flowMap = FlowMap()
        flowMap.entries.addAll(flowMapping!!)
        return flowMap
    }
}