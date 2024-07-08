package lib.subscribers.producer;

public interface IProducer<T> {
    void produce(T content);
}
