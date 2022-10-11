package transaction.stage2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class SecondUserService {

    private static final Logger log = LoggerFactory.getLogger(SecondUserService.class);

    private final UserRepository userRepository;

    public SecondUserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String saveSecondTransactionWithRequired() {
        userRepository.save(User.createTest());
        logActualTransactionActive();
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String saveSecondTransactionWithRequiresNew() {
        userRepository.save(User.createTest());
        logActualTransactionActive();
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public String saveSecondTransactionWithSupports() {
        userRepository.save(User.createTest());
        logActualTransactionActive();
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public String saveSecondTransactionWithMandatory() {
        userRepository.save(User.createTest());
        logActualTransactionActive();
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String saveSecondTransactionWithNotSupported() {
        userRepository.save(User.createTest());
        logActualTransactionActive();
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.NESTED)
    public String saveSecondTransactionWithNested() {
        userRepository.save(User.createTest());
        logActualTransactionActive();
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.NEVER)
    public String saveSecondTransactionWithNever() {
        userRepository.save(User.createTest());
        logActualTransactionActive();
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    private void logActualTransactionActive() {
        final var currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        final var actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        final var emoji = actualTransactionActive ? "✅" : "❌";
        log.info("\n{} is Actual Transaction Active : {} {}", currentTransactionName, emoji, actualTransactionActive);
    }
}
