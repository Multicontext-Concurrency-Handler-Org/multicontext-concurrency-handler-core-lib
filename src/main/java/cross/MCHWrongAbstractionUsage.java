package cross;

/**
 * Custom exception class designed to signal the improper use of abstractions within the MCH feature.
 * This class is intended for situations where the developer has used a wrong design or implementation
 * abstraction that violates the intended architecture or design principles of the system.
 *
 * This exception should be thrown when an object is unexpectedly null in an implementation-specific
 * scenario, to clearly communicate the intention and explain the reason behind the check.
 *
 * <p>
 * In cases where the issue arises in the domain layer, prefer to use
 * {@code domain.error.DomainErrorException} to represent domain-related errors.
 * However, when the non-null check is related to implementation details or other
 * areas not directly tied to domain logic, use the {@code assertNonNull} method
 * provided in this class. This method allows you to specify the reason for the check,
 * ensuring future developers can understand why the non-null constraint exists in that context.
 * </p>
 *
 * Example usage:
 * <pre>
 * {@code
 * MCHWrongAbstractionUsage.assertNonNull(someObject, "someObject must not be null because...");
 * }
 * </pre>
 *
 * This ensures that errors related to the misuse of abstractions or other critical implementation
 * details are caught early and provide meaningful feedback for developers.
 */
public class MCHWrongAbstractionUsage extends RuntimeException {
    public MCHWrongAbstractionUsage(String message) {
        super(message);
    }

    /**
     * Asserts that the specified object is not null. If the object is null, this method
     * throws an {@code MCHWrongAbstractionUsage} exception with a detailed message explaining why
     * the object must not be null in the current context.
     *
     * @param <T>    the type of the object to be checked
     * @param obj    the object to check for null
     * @param reason the explanation for why the object must not be null
     * @throws MCHWrongAbstractionUsage if the object is null, along with the provided reason
     */
    public static <T> void assertNonNull(T obj, String reason) {
        if (obj == null)
            throw new MCHWrongAbstractionUsage(reason);
    }
}
