package messagechat.messagechatservice.data.core;

import lombok.Cleanup;
import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.data.common.AbstractTest;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.Message;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MessageChatJpaConfig.class})
public class IsolationTransactionTest extends AbstractTest {

    private static final String DIALOG_ID = "test-dialog-1";
    private static final String MESSAGE_ID = "test-message-1";

    @Resource
    private EntityManagerFactory entityManagerFactory;


    /**
     * Prevent Last Commit wins effect by Optimistic Lock:
     */
    @Test
    void throw_exception_by_optimistic_lock_when_try_to_change_dialog_name() {
        createDialogForCouple((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);

        final String F_D_CH = "First dialog NAME changing";
        @Cleanup final Session session1 = (Session) entityManagerFactory.createEntityManager();
        @Cleanup final Session session2 = (Session) entityManagerFactory.createEntityManager();

        //validate:
        Exception ex = assertThrows(RollbackException.class, () -> {
            session1.beginTransaction();
            session2.beginTransaction();

            //User 1:
            var dialog = session1.find(Dialog.class, 1, LockModeType.OPTIMISTIC);
            dialog.setName(F_D_CH);

            //User 2:
            var theSamedialog = session2.find(Dialog.class, 1, LockModeType.OPTIMISTIC);
            theSamedialog.setName("Second dialog NAME changing");

            //Thanks to Optimistic Locking -> First commit win:
            session1.getTransaction().commit();
            session2.getTransaction().commit();
        });
        assertTrue(ex.getCause() instanceof OptimisticLockException);
        var dialog = readDialogByDialogIdWithFetchedMembers((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);
        assertEquals(F_D_CH, dialog.getName());

        cleanDb((Session) entityManagerFactory.createEntityManager());
    }

    @Test
    @Disabled //TODO: Remove annotation after fixing issue with deadlock.
    void pessimistic_lock_test() {
        createDialogForCouple((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);

        final String F_M_CH = "First message DESCRIPTION changing";
        @Cleanup final Session session1 = (Session) entityManagerFactory.createEntityManager();
        @Cleanup final Session session2 = (Session) entityManagerFactory.createEntityManager();

        session1.beginTransaction();
        session2.beginTransaction();

        try {


            //User 1:
//            Message message = session1.createQuery(
//                            "select m from Message m " +
//                                    "join fetch m.author a " +
//                                    "join fetch a.memberInfo m_inf " +
//                                    "where m.messageId = :messageId ", Message.class)
//                    .setParameter("messageId", MESSAGE_ID)
//                    .setLockMode(LockModeType.PESSIMISTIC_READ)
//                    .setHint(JPA_LOCK_TIMEOUT, 2000)
//                    .getSingleResult();
            LockOptions lockOptions = new LockOptions(LockMode.PESSIMISTIC_READ);
            lockOptions.setTimeOut(1000);
            Message message = session1.get(Message.class, 1, lockOptions);
            message.setDescription(F_M_CH);

            //User 2:
            Message theSameMessage = session2.createQuery(
                            "select m from Message m " +
                                    "join fetch m.author a " +
                                    "join fetch a.memberInfo m_inf " +
                                    "where m.messageId = :messageId ", Message.class)
                    .setParameter("messageId", MESSAGE_ID)
                    .getSingleResult();
            theSameMessage.setDescription("Second message DESCRIPTION changing");

            //In this case just second user must waite until first will finish transaction:
            session2.getTransaction().commit();
            session1.getTransaction().commit();

        } catch (Exception ex) {
            System.out.println("dfgdf");
        }

        System.out.println("sdfg");
    }
}