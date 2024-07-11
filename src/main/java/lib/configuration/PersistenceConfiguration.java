package lib.configuration;

import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;
import domain.repository.PersistenceContext;

public record PersistenceConfiguration(IProcessRepository processRepository, ILockRepository lockRepository) {
    public PersistenceContext toPersistenceContext() {
        return new PersistenceContext(
                this.processRepository,
                this.lockRepository
        );
    }
}