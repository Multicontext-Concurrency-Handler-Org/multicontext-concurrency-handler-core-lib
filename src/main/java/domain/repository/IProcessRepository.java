package domain.repository;

import domain.entity.Process;

import java.util.List;

public interface IProcessRepository {
    /**
     * Get concurrent processes, if is STW return all processes
     * @param process
     * @returns     The list of concurrent processes
     */
    List<Process> getConcurrentProcesses(Process process);
}
