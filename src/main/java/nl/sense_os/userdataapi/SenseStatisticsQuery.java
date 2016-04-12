package nl.sense_os.userdataapi;

/**
 * Created by tatsuya on 24/03/16.
 */
public class SenseStatisticsQuery {

    private Long startTime = null;
    private Long endTime = null;
    private String sortOrder = null;
    private Integer limit = null;
    private String period = null;
    private String aggregation = null;
    private Boolean running = null;

    public Long getStartTime() {
        return startTime;
    }

    public SenseStatisticsQuery setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public SenseStatisticsQuery setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public SenseStatisticsQuery setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public SenseStatisticsQuery setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Boolean getRunning() {
        return running;
    }

    public SenseStatisticsQuery setRunning(Boolean running) {
        this.running = running;
        return this;
    }
}
