package lib.commands.engine.dto;

import domain.enums.StateChange;

import java.util.List;

public record StateChangeEventDTO(
        StateChange event,
        List<String> locks
) {
}
