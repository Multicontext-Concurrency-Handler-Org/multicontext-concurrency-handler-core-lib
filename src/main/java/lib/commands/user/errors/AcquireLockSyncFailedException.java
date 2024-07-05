package lib.commands.user.errors;

public class AcquireLockSyncFailedException extends RuntimeException {
    public final Boolean isRetryable;
    public AcquireLockSyncFailedException(Boolean isRetryable) {
        super("acquire lock sync failed");
        this.isRetryable = isRetryable;
    }
}
