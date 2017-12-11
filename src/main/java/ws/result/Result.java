package ws.result;

/**
 * Created by zl on 2017/6/23
 */
public class Result {

    /**
     * 状态码 SUCCESS：成功 FAIL：失败
     */
    private String code;

    /**
     * 失败原因描述
     */
    private String failMessage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }
}
