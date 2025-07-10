package com.graphhopper.application.cli;

import com.graphhopper.application.ev.AvgSpeedConditional;
import com.graphhopper.application.parsers.AvgSpeedConditionalParser;
import com.graphhopper.routing.ev.DecimalEncodedValueImpl;
import com.graphhopper.routing.ev.DefaultImportRegistry;
import com.graphhopper.routing.ev.ImportUnit;

public class CustomImportRegistry extends DefaultImportRegistry {
    @Override
    public ImportUnit createImportUnit(String name) {

        if (AvgSpeedConditional.KEYS.contains(name)) {
            return ImportUnit.create(
                    name,
                    props -> new DecimalEncodedValueImpl(name, 7, 2, true),
                    (lookup, props) -> new AvgSpeedConditionalParser(
                            name,
                            lookup.getDecimalEncodedValue(name)));
        }
        return super.createImportUnit(name);
    }
}