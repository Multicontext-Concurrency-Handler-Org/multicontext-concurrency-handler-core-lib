package utils.repositories;

import domain.entity.Process;
import domain.entity.vos.process.ConcurrencyVO;
import domain.repository.IProcessRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryProcessRepository implements IProcessRepository {
    private final List<Process> processes = new ArrayList<>();

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    @Override
    public List<String> getConcurrentProcesses(Process process) {
        return process.getIsStopTheWorld() ?
                this.processes.stream().map(Process::getName).collect(Collectors.toList()) :
                process.getConcurrencies().stream().map(concurrencyVO -> concurrencyVO.process().getName()).collect(Collectors.toList());
    }

    @Override
    public Optional<Process> findByName(String process) {
        return processes.stream().findAny().filter(p -> p.getName().equals(process));
    }
}
