package domain.repository;

import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;

public record PersistenceContext(
        IProcessRepository processRepository,
        ILockRepository lockRepository
) { }
