package exceptions;

public class CustomDuplicatedElementException extends RuntimeException {
    public CustomDuplicatedElementException() {
        this("중복된 원소가 있음");
    }

    public CustomDuplicatedElementException(String message) {
        super(message);
    }
}