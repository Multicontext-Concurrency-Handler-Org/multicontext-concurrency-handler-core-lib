package lib.usecase;

import domain.error.DomainErrorException;
import domain.error.UnexpectedErrorException;
import lib.exceptions.MCHConstraintViolation;
import lib.exceptions.MCHConstraintViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MCHUseCase<T> {
    public void call(T dto) throws MCHConstraintViolationException {
        var constraintViolations = this.validate(dto);
        MCHConstraintViolation.throwConstraintViolations(constraintViolations);

        try {
            this.execute(dto);
        } catch (DomainErrorException domainErrorException) {
            domainErrorException.throwEvent();
        } catch (Exception e) {
            var domainErrorHandler = new UnexpectedErrorException(e);
            domainErrorHandler.throwEvent();
        }
    }

    protected abstract void execute(T dto) throws DomainErrorException;

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
