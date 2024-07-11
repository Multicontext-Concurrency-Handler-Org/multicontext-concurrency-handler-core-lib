package domain.services;

import domain.entity.Process;
import domain.exceptions.DomainErrorException;
import domain.exceptions.MissingConfigurationException;
import domain.repository.PersistenceContext;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
