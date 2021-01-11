package com.crazzyghost.alphavantage.indicator.response.atr;

import com.crazzyghost.alphavantage.indicator.response.PeriodicResponse;
import com.crazzyghost.alphavantage.indicator.response.SimpleIndicatorUnit;
import com.crazzyghost.alphavantage.parser.Parser;

import java.util.List;
import java.util.Map;

public class ATRResponse extends PeriodicResponse {

    private ATRResponse(List<SimpleIndicatorUnit> indicatorUnits, MetaData metaData) {
        super(indicatorUnits, metaData);
    }

    private ATRResponse(String errorMessage) {
        super(errorMessage);
    }

    public static ATRResponse of(Map<String, Object> stringObjectMap){
        Parser<ATRResponse> parser = new ATRParser();
        return parser.parse(stringObjectMap);
    }

    public static class ATRParser extends PeriodicParser<ATRResponse> {

        @Override
        public ATRResponse get(List<SimpleIndicatorUnit> indicatorUnits, MetaData metaData) {
            return new ATRResponse(indicatorUnits, metaData);
        }

        @Override
        public ATRResponse get(String errorMessage) {
            return new ATRResponse(errorMessage);
        }

        @Override
        public String getIndicatorKey() {
            return "ATR";
        }
    }
}
