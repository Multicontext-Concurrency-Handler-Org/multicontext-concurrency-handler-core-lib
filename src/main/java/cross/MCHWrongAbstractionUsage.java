package cross;

/**
 * Just a very explicit way to denote intention when writing your MCH feature
 * Always prefer to use
 *  domain.error.DomainErrorException; (for Domain context)
 *<p></p>
 * but if it's an implementation detail that you only want to make sure and let other developers
 * to understand your intention, throw a requireNonNullWithReason and explain why it should
 */
public class MCHWrongAbstractionUsage extends RuntimeException {
    public MCHWrongAbstractionUsage(String message) {
        super(message);
    }
    public static <T> void assertNonNull(T obj, String reason) {
        if (obj == null)
            throw new MCHWrongAbstractionUsage(reason);
    }
}
