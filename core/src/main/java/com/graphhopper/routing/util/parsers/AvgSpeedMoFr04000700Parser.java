package com.graphhopper.routing.util.parsers;

import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.EdgeIntAccess;
import com.graphhopper.routing.util.parsers.TagParser;
import com.graphhopper.storage.IntsRef;
import com.graphhopper.routing.ev.AvgSpeedMoFr04000700;

public class AvgSpeedMoFr04000700Parser implements TagParser {
    private final DecimalEncodedValue avgSpeedEnc;

    public AvgSpeedMoFr04000700Parser(DecimalEncodedValue avgSpeedEnc) {
        this.avgSpeedEnc = avgSpeedEnc;
    }

    @Override
    public void handleWayTags(int edgeId, EdgeIntAccess edgeIntAccess, ReaderWay way, IntsRef relationFlags) {
        // Parse custom tags for average speed during Mo-Fr 04:00-07:00
        String avgSpeedTag = way.getTag("avgspeed_Mo_Fr_0700_0900_forward");
        
        if (avgSpeedTag != null) {
            try {
                double speed = Double.parseDouble(avgSpeedTag);
                avgSpeedEnc.setDecimal(false, edgeId, edgeIntAccess, speed);
            } catch (NumberFormatException e) {
                // If parsing fails, use default speed
                avgSpeedEnc.setDecimal(false, edgeId, edgeIntAccess, AvgSpeedMoFr04000700.DEFAULT_SPEED);
            }
        } else {
            // No data available, use default
            avgSpeedEnc.setDecimal(false, edgeId, edgeIntAccess, AvgSpeedMoFr04000700.DEFAULT_SPEED);
        }
    }
}
