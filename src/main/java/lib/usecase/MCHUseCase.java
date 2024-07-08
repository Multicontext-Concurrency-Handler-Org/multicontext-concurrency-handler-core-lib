package lib.usecase;

import lib.exceptions.MCHConstraintViolation;
import lib.exceptions.MCHConstraintViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MCHUseCase<T> {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public void call(T dto) throws MCHConstraintViolationException {
        var constraintViolations = this.validate(dto);
        MCHConstraintViolation.throwConstraintViolations(constraintViolations);

        this.execute(dto);
    }

    protected abstract void execute(T dto);

    protected List<MCHConstraintViolation> validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        return violations.stream()
                .map(violation -> new MCHConstraintViolation(violation.getMessage()))
                .collect(Collectors.toList());
    }
}
