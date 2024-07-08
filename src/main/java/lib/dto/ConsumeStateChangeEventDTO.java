package lib.dto;

import domain.enums.StateChange;

import java.util.List;

public record ConsumeStateChangeEventDTO(
        Integer version,
        StateChange event,
        List<String> locks
) {
}
