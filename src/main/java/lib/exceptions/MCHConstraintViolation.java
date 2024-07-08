package lib.exceptions;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class MCHConstraintViolation {
    private final String message;

    public static void throwConstraintViolations(List<MCHConstraintViolation> mchConstraintViolations) throws MCHConstraintViolationException {
        Objects.requireNonNull(mchConstraintViolations, "List<MCHValidationError> must not be null");
        if(!mchConstraintViolations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Validation Errors: ");
            for(int i = 0; i < mchConstraintViolations.size(); i++) {
                sb.append(String.format("%s", mchConstraintViolations.get(i)));
                if(i < mchConstraintViolations.size() - 1) {
                    sb.append(", ");
                }
            }
            String message = sb.toString();
            throw new MCHConstraintViolationException(message);
        }
    }
}
