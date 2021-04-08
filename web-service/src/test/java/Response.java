public class Response<T> {
    final int code;
    final T body;

    public Response(int code, T body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public T getBody() {
        return body;
    }
}
