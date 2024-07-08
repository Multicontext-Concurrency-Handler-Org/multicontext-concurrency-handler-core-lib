package utils;

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
    public List<Process> getConcurrentProcesses(Process process) {
        return process.getIsStopTheWorld() ?
                this.processes :
                process.getConcurrencies().stream().map((ConcurrencyVO::process)).collect(Collectors.toList());
    }

    @Override
    public Optional<Process> findProcessByName(String process) {
        return processes.stream().findAny().filter(p -> p.getName().equals(process));
    }
}
