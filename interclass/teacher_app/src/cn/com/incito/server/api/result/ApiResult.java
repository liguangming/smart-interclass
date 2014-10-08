package cn.com.incito.server.api.result;

public class ApiResult {
	public static final int SUCCESS = 0;
	
	private int code;
	private String message;
	private IApiResultData data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public IApiResultData getData() {
		return data;
	}

	public void setData(IApiResultData data) {
		this.data = data;
	}

}
