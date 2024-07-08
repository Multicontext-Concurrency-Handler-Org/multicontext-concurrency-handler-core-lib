package utils;

public interface IHandleEventLambda<T> {
    void execute(T eventContent);
}
