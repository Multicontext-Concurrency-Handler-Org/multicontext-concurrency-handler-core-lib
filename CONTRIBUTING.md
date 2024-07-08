# Contributing to MCH Core Lib

By submitting a pull request or a patch, you represent that you have the right to license your contribution to the MCH Core Lib project owners and the community, agree that your contributions are licensed under the MCH Core Lib license, and agree to future changes to the licensing.

## Logs

The general recommendation is not introduce logs that you didn't use during your development process or don't attend to any specific use case that will be used on production
avoid exaggerating on new logs, that can lead to performance issues and increase log storage costs.

| Log Level | Description                                                                                                  | Past Tense   | Run on production |
|-----------|--------------------------------------------------------------------------------------------------------------|--------------|-------------------|
| TRACE     | Should be used to trace code execution path for complex scenarios                                            | Not Required | no                |
| DEBUG     | Should be used to help debugging of business logic                                                           | Not Required | no                |
| INFO      | Should be used to notify some state or milestone was achieved by the program                                 | Always       | yes               |
| WARN      | Should be used to notify invalid or unexpected states that the program was able to recover from              | Always       | yes               |
| ERROR     | Should be used for non critical errors, something that will not stop the overall functioning of the system   | Always       | yes               |
| CRITICAL  | Should be used for extreme scenarios where the functioning, security or consistency of the system is in risk | Not required | yes               |

### Trace Logs

Trace logs can provide useful information why some piece of logic was executed or not but doesnt affect directly the business logic
here in this example searching for all pending locks instead of first searching for concurrent process is a performance optimization
when the process is STW

Code example:
```java
if(lock.getProcess().getIsStopTheWorld()) {
    logger.trace("STW processes don't need concurrency filtering");
    return getAllPendingLocksOrderedByPriority();
}
```

### Debug Logs

Debug logs will provide useful information about business logic, helping the developer to understand the code behaviour

Code example:
```java
if (Boolean.TRUE.equals(this.persistenceContext.lockRepository().hasStopTheWorldLockOnRunningStatus())) {
    logger.debug("Won't be able to start running since there is a STW lock running");
    return;
}
```

### Info Logs

Info logs can signal a state change on the system our that some step finished executing helping operate the software in production

Code example:
```java
Lock acquiredLock = this.persistenceContext.lockRepository().updateToRunning(lockId);
logger.info(String.format("Lock %s started running", lockId));
```

### Warn Logs

Warn logs should be used to signal an invalid, unconventional or unexpected state, for this example we expect the injected
dependency to behave some way, and if it doesn't the code knows how to recover from it, but will warn it, because this may
generate an unexpected error in the future

Code example:
```java
var concurrentProcesses = this.persistenceContext.processRepository().getConcurrentProcesses(lock.getProcess());
if(Objects.isNull(concurrentProcesses)) {
    logger.warn("Unexpected behaviour IProcessRepository.getConcurrentProcesses(Process process) returned null instead of an empty list");
    return new ArrayList<>();
}
```

### Error Logs

Error logs will tell the user when the system reached an invalid state

Code example:
```java
var lock = this.persistenceContext.lockRepository().findLockById(lockId);
if(lock.isEmpty()) {
    logger.error(String.format("No lock was found by id %s", lockId));
    return;
}
```

### Critical Logs

I don't have any examples right now, but I imagine that this can be used if you reach some scenario that seems like exploding the computer would be safer

Thanks for contributing!