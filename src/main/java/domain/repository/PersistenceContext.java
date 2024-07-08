package domain.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record PersistenceContext(
        IProcessRepository processRepository,
        ILockRepository lockRepository
) {
    private static final Logger logger = LogManager.getLogger();

    public interface IAccessExclusiveLockLambda {
        void execute();
    }

    /**
     * This method enable working with multithreading, multiprocessing or even multi instances (like in kubernetes)
     * without risking the concurrency model
     *
     * @param callback Concurrency Safe CallBack
     */
    public void executeWithinAccessExclusiveLockContext(IAccessExclusiveLockLambda callback) {
        this.lockRepository.acquireAccessExclusiveLock();

        try {
            callback.execute();
        } catch (Exception e) {
            logger.error("Unexpected exception caught " + e.getMessage());
        } finally {
            this.lockRepository.releaseAccessExclusiveLock();
        }
    }
}
