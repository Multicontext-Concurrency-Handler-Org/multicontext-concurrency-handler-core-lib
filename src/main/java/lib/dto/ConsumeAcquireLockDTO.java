package lib.dto;

import org.apache.logging.log4j.util.PerformanceSensitive;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public record ConsumeAcquireLockDTO(

        @NotNull @Positive Integer version,
        @NotNull @NotEmpty @Size(max=200) String process,

        @PerformanceSensitive(
            """
                This will probably be sent over kafka, rabbitmq, pub/sub, sqs, redis
                or another queue, pub/sub, streaming provider.
                
                16_000 items with 32 characters assuming 1 byte for each ascii char
                it will take 16_000 * 32 bytes that is approximately half an MB leaving
                another half of MB for the context Object
                
                The default max message size config for kafka is 1MB
            """
        )
        @Size(max = 16_000) List<@NotNull @Size(min=32, max=32) String> workingSet,

        @PerformanceSensitive(
            """
                This will probably be sent over kafka, rabbitmq, pub/sub, sqs, redis
                or another queue, pub/sub, streaming provider.
                
                If the Object Size exceed Half a MB there's a high change of generating
                an error while trying to produce the message on some king of
                queue, pub/sub or streaming provider.
                
                The default max message size config for kafka is 1MB
            """
        )
        Object context
) { }
