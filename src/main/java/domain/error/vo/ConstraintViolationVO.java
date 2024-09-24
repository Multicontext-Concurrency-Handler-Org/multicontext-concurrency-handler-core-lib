package domain.error.vo;

import java.util.List;
import java.util.stream.Collectors;

import static cross.MCHWrongAbstractionUsage.assertNonNull;

public record ConstraintViolationVO(String message) {
    public static String summarize(List<ConstraintViolationVO> constraintViolationVOs) {
        assertNonNull(constraintViolationVOs, "mchConstraintViolations must not be null");

        return constraintViolationVOs
            .stream()
            .map(ConstraintViolationVO::message)
            .collect(Collectors.joining(", "));
    }
}
