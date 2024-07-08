package lib.dto;

import domain.enums.StateChange;
import org.apache.logging.log4j.util.PerformanceSensitive;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public record ConsumeStateChangeEventDTO(
        @NotNull @Positive Integer version,
        @NotNull StateChange event,
        @PerformanceSensitive(
            """
                This will probably be sent over kafka, rabbitmq, pub/sub, sqs, redis
                or another queue, pub/sub, streaming provider.
                30_000 items with 32 characters assuming 1 byte for each ascii char
                it will take 30_000 * 32 bytes that is barely less than 1MB
                The default max message size config for kafka is 1MB
            """
        )
        @Size(max=30_000) List<@NotNull @Size(min=32, max=32) String> locks
) {
}
