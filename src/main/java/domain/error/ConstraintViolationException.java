package domain.error;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.error.vo.ConstraintViolationVO;
import domain.event.impls.DomainErrorEvent;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
public class ConstraintViolationException extends DomainErrorException {
    List<ConstraintViolationVO> constraintViolationVOs;

    @Override
    protected DomainErrorEvent toEvent() {
        return new DomainErrorEvent(
                new DomainErrorEventVO(
                        DomainErrorType.INVALID_DATA,
                        ConstraintViolationVO.summarize(this.constraintViolationVOs),
                        Instant.now()
                )
        );
    }
}
