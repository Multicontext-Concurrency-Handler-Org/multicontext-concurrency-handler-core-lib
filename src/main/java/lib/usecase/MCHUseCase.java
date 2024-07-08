package lib.usecase;

import lib.errors.MCHConstraintViolation;
import lib.errors.MCHConstraintViolationException;

import java.util.List;

public abstract class MCHUseCase<T> {
    public void call(T dto) throws MCHConstraintViolationException {
        var constraintViolations = this.validate(dto);
        MCHConstraintViolation.throwConstraintViolations(constraintViolations);

        this.execute(dto);
    }

    protected abstract void execute(T dto);
    protected abstract List<MCHConstraintViolation> validate(T dto);
}
