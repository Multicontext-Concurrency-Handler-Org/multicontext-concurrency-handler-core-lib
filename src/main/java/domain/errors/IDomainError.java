package domain.errors;

import domain.event.impls.DomainErrorEvent;

public interface IDomainError {
    DomainErrorEvent toEvent();
}
