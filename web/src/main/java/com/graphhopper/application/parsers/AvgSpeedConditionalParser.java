package com.graphhopper.application.parsers;

import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.EdgeIntAccess;
import com.graphhopper.routing.util.parsers.TagParser;
import com.graphhopper.storage.IntsRef;

import java.util.regex.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvgSpeedConditionalParser implements TagParser {
    private static final Logger logger = LoggerFactory.getLogger(AvgSpeedConditionalParser.class);
    private final String encodedValueKey;
    private final DecimalEncodedValue avgSpeedEnc;

    public AvgSpeedConditionalParser(String encodedValueKey, DecimalEncodedValue avgSpeedEnc) {
        this.encodedValueKey = encodedValueKey;
        this.avgSpeedEnc = avgSpeedEnc;
    }

    @Override
    public void handleWayTags(int edgeId, EdgeIntAccess edgeIntAccess, ReaderWay way, IntsRef relationFlags) {
        processConditionalTag(edgeId, edgeIntAccess, way, true, relationFlags);
        processConditionalTag(edgeId, edgeIntAccess, way, false, relationFlags);
    }

    private void processConditionalTag(int edgeId, EdgeIntAccess edgeIntAccess, ReaderWay way, Boolean reverse,
            IntsRef relationFlags) {
        String tagKey = reverse ? "avgspeed:backward:conditional" : "avgspeed:forward:conditional";
        String tag = way.getTag(tagKey);
        if (tag == null)
            return;

        Pattern entryPattern = Pattern.compile("([\\d.]+)\\s*@\\s*\\(([^)]+)\\)");
        Matcher matcher = entryPattern.matcher(tag);

        while (matcher.find()) {
            logger.info("Found matcher group: {}", matcher.group(0));
            double speed = Double.parseDouble(matcher.group(1));
            String condition = matcher.group(2).toLowerCase().replaceAll("[^a-z0-9:_]", "_").replace(":", "");

            String conditionKey = reverse ? "avgspeed_" + condition + "_backward"
                    : "avgspeed_" + condition + "_forward";
            logger.info("Parsed key: '{}', encodedValueKey: '{}', speed: {}", conditionKey, encodedValueKey, speed);

            if (conditionKey.equals(encodedValueKey + "_forward") && !reverse) {
                logger.info("Set FORWARD speed {} for edge {} with key {}", speed, edgeId, encodedValueKey);
                avgSpeedEnc.setDecimal(false, edgeId, edgeIntAccess, speed);
            } else if (conditionKey.equals(encodedValueKey + "_backward") && reverse) {
                logger.info("Set BACKWARD speed {} for edge {} with key {}", speed, edgeId, encodedValueKey);
                avgSpeedEnc.setDecimal(true, edgeId, edgeIntAccess, speed);
            }
        }
        return;
    }
}