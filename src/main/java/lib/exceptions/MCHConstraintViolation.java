package lib.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static cross.MCHWrongAbstractionUsage.requireArgumentNonNull;

public record MCHConstraintViolation(String message) {
    public static void throwConstraintViolations(List<MCHConstraintViolation> mchConstraintViolations) throws MCHConstraintViolationException {
        requireArgumentNonNull(mchConstraintViolations, "mchConstraintViolations");

        if (!mchConstraintViolations.isEmpty()) {
            throw new MCHConstraintViolationException(mchConstraintViolations
                    .stream()
                    .map(MCHConstraintViolation::message)
                    .collect(Collectors.joining(", "))
            );
        }
    }
}
