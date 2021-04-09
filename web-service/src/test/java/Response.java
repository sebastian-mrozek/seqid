public class Response<T> {
    public final int code;
    public final T body;

    public Response(int code, T body) {
        this.code = code;
        this.body = body;
    }
}
