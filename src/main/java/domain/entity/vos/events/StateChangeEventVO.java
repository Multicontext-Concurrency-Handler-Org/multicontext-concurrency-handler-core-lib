package domain.entity.vos.events;

import domain.enums.StateChange;

import java.util.List;

public record StateChangeEventVO(
        StateChange event,
        List<String> locks
) { }
