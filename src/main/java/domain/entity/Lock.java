package domain.entity;

import domain.entity.vos.events.AcquireLockEventVO;
import domain.entity.vos.events.LockAcquiredEventVO;
import domain.enums.LockStatus;
import domain.entity.vos.lock.RunningDetailsVO;
import domain.event.EventPublisher;
import domain.event.impls.LockAcquiredEvent;
import domain.repository.ILockRepository;
import domain.repository.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
public class Lock {
    private String id;
    private Integer version;
    private LockStatus status;
    private Process process;
    private RunningDetailsVO runningDetailsVO;
    private Instant createdAt;
    private Instant updatedAt;
    private Date partitionDate;
    private Object context;
    private List<String> workingSet;
}
