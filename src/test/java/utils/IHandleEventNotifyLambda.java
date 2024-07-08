package utils;

public interface IHandleEventNotifyLambda<T> {
    void execute(T eventContent);
}
