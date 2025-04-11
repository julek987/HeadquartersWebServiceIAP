package IAP.exception;

public class RichErrorResponse {

    private int status;
    private String title;
    private String detail;
    private String instance;

    public RichErrorResponse(int status, String title, String detail, String instance) {
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
