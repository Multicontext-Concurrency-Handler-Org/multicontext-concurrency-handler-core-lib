package domain.repository;

import domain.entity.Process;

import java.util.List;
import java.util.Optional;

public interface IProcessRepository {
    /**
     * Get concurrent processes, if is STW return all processes
     *
     * @param process Process that if its executing blocks the concurrent processes
     * @return     The list of concurrent processes
     */
    List<Process> getConcurrentProcesses(Process process);

    /**
     * Find process by name
     * @param process processName
     * @return process if exists
     */
    Optional<Process> findProcessByName(String process);
}
