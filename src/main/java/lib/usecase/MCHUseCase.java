package lib.usecase;

import domain.errors.DomainErrorHandler;
import domain.errors.UnexpectedErrorHandler;
import lib.exceptions.MCHConstraintViolation;
import lib.exceptions.MCHConstraintViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MCHUseCase<T> {
    public void call(T dto) throws MCHConstraintViolationException {
        var constraintViolations = this.validate(dto);
        MCHConstraintViolation.throwConstraintViolations(constraintViolations);

        try {
            this.execute(dto);
        } catch (Exception e) {
            var domainErrorHandler = new UnexpectedErrorHandler(e);
            domainErrorHandler.throwEvent();
        }
    }

    protected abstract void execute(T dto);

    protected List<MCHConstraintViolation> validate(T dto) {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            var validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(dto);

            return violations.stream()
                    .map(violation -> new MCHConstraintViolation(violation.getMessage()))
                    .collect(Collectors.toList());
        }
    }
}
