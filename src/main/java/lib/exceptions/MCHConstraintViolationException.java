package lib.exceptions;


import cross.MCHWrongAbstractionUsage;

public class MCHConstraintViolationException extends MCHWrongAbstractionUsage {
    public MCHConstraintViolationException(String message) {
        super(message);
    }
}
