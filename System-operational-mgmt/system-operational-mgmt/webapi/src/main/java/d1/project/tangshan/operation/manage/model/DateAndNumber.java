package d1.project.tangshan.operation.manage.model;

/**
 * @author lin
 */
public class DateAndNumber {
    private String date;
    private Long number;

    public DateAndNumber() {
    }

    public DateAndNumber(String date, Long number) {
        this.date = date;
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
