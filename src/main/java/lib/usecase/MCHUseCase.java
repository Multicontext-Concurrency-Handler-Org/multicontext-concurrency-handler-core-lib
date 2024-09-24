package lib.usecase;

import domain.error.ConstraintViolationException;
import domain.error.DomainErrorException;
import domain.error.UnexpectedErrorException;
import domain.error.vo.ConstraintViolationVO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MCHUseCase<T> {
    public void call(T dto) {
        try {
            this.validateOrThrow(dto);
            this.execute(dto);
        } catch (DomainErrorException domainErrorException) {
            domainErrorException.throwEvent();
        } catch (Exception e) {
            var domainErrorHandler = new UnexpectedErrorException(e);
            domainErrorHandler.throwEvent();
        }
    }

    protected abstract void execute(T dto) throws DomainErrorException;

    protected void validateOrThrow(T dto) throws ConstraintViolationException {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            var validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(dto);

            List<ConstraintViolationVO> constraintViolations = violations.stream()
                    .map(violation -> new ConstraintViolationVO(violation.getMessage()))
                    .collect(Collectors.toList());

            if(!constraintViolations.isEmpty()) {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
    }
}
