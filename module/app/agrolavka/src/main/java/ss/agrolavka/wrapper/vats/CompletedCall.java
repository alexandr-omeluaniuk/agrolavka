package ss.agrolavka.wrapper.vats;

import java.util.List;

public class CompletedCall {

    private String externalId;

    private String number;

    private MySkladRef counterparty;

    private String extension;

    private MySkladRef employee;

    private Boolean isIncoming;

    private String startTime;

    private String endTime;

    private Long duration;

    private List<String> recordUrl;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public MySkladRef getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(MySkladRef counterparty) {
        this.counterparty = counterparty;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public MySkladRef getEmployee() {
        return employee;
    }

    public void setEmployee(MySkladRef employee) {
        this.employee = employee;
    }

    public Boolean getIncoming() {
        return isIncoming;
    }

    public void setIncoming(Boolean incoming) {
        isIncoming = incoming;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<String> getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(List<String> recordUrl) {
        this.recordUrl = recordUrl;
    }
}
