package domain.services;

import domain.entity.Process;
import domain.error.DomainErrorException;
import domain.error.MissingConfigurationException;
import domain.repository.PersistenceContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProcessService {
    private final PersistenceContext persistenceContext;

    public Process findByNameOrThrow(String process) throws DomainErrorException {
        var processOpt = persistenceContext.processRepository().findByName(process);

        if(processOpt.isEmpty()) {
            throw new MissingConfigurationException("process %s not found by name");
        }

        return processOpt.get();
    }
}
