package domain.entity.vos.events;

import domain.enums.StateChange;

import java.util.List;

public record StateChangeEventVO(
        Integer version,
        StateChange event,
        List<String> locks
) { }
