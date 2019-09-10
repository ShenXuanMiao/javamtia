package io.github.viscent.mtia.ch3;

public class HTTPRangeRequest {
    private final Range range;
    private String url;

    public HTTPRangeRequest(String url, int lowerBound, int upperBound) {
        this.url = url;
        this.range = new Range(lowerBound, upperBound);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Range getRange() {
        return range;
    }

    public static class Range {
        private long lowerBound;
        private long upperBound;

        public Range(long lowerBound, long upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public long getLowerBound() {
            return lowerBound;
        }

        public void setLowerBound(long lowerBound) {
            this.lowerBound = lowerBound;
        }

        public long getUpperBound() {
            return upperBound;
        }

        public void setUpperBound(long upperBound) {
            this.upperBound = upperBound;
        }

    }
}