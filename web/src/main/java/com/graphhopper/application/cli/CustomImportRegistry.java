/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.graphhopper.application.cli;

import com.graphhopper.application.ev.AvgSpeedMoFr04000700;
import com.graphhopper.application.parsers.AvgSpeedMoFr04000700Parser;
import com.graphhopper.routing.ev.DefaultImportRegistry;
import com.graphhopper.routing.ev.ImportUnit;


public class CustomImportRegistry extends DefaultImportRegistry {
    @Override
    public ImportUnit createImportUnit(String name) {
        if (AvgSpeedMoFr04000700.KEY.equals(name))
            return ImportUnit.create(name, props -> AvgSpeedMoFr04000700.create(),
                    (lookup, props) -> new AvgSpeedMoFr04000700Parser(
                        lookup.getDecimalEncodedValue(AvgSpeedMoFr04000700.KEY))
            );
        return super.createImportUnit(name);
    }
}
