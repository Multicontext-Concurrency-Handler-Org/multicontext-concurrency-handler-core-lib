package lib.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import static cross.MCHWrongAbstractionUsage.assertNonNull;

public record MCHConstraintViolation(String message) {
    public static void throwConstraintViolations(List<MCHConstraintViolation> mchConstraintViolations) throws MCHConstraintViolationException {
        assertNonNull(mchConstraintViolations, "mchConstraintViolations must not be null");

        if (!mchConstraintViolations.isEmpty()) {
            throw new MCHConstraintViolationException(mchConstraintViolations
                    .stream()
                    .map(MCHConstraintViolation::message)
                    .collect(Collectors.joining(", "))
            );
        }
    }
}
