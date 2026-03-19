package messagechat.messagechatservice.data.core.isolation;

import lombok.Cleanup;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.data.common.AbstractTest;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.persistent.entity.Dialog;
import org.hibernate.Session;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MessageChatJpaConfig.class, MessageChatConfigProps.class})
@MockBean(CacheManagerImpl.class) // in order to run a ctx
public class OptimisticLockTest extends AbstractTest {

    private static final String DIALOG_ID = "test-dialog-1";
    private static final String MESSAGE_ID = "test-message-1";

    private static final String F_D_CH = "Dialog Name - First update";
    private static final String S_D_CH = "Dialog Name - Second update";

    @Resource
    private EntityManagerFactory entityManagerFactory;


    /**
     * Prevent "Last Commit wins" effect using Optimistic Lock.
     *
     * No inner Entities.
     */
    @Test
    void throwException_byOptimisticLock_whenTryToChangeDialogName() {
        createDialogForCouple((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);

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
            theSamedialog.setName(S_D_CH);

            // Thanks to Optimistic Locking, First Commit will win.
            // Changes from second session will be ignored and app will throw ex because of that :
            session1.getTransaction().commit();
            session2.getTransaction().commit();
        });
        assertTrue(ex.getCause() instanceof OptimisticLockException);
        var dialog = readDialogByDialogIdWithFetchedMembers((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);
        assertEquals(F_D_CH, dialog.getName());

        cleanDb((Session) entityManagerFactory.createEntityManager());
    }


    /**
     * Prevent "Last Commit wins" effect using Optimistic Lock.
     *
     * Fetch inner Entities.
     */
    @Test
    @Disabled
    // todo: fix "org.hibernate.HibernateException: Unable to perform beforeTransactionCompletion callback: null"
    void throwException_byOptimisticLock_whenTryToChangeDialogMemberData_withInnerEntity() {
        createDialogForCouple((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);

        //validate:
        Exception ex = assertThrows(RollbackException.class, () -> {
            try (Session session1 = (Session) entityManagerFactory.createEntityManager();
                 Session session2 = (Session) entityManagerFactory.createEntityManager()) {

                session1.beginTransaction();
                session2.beginTransaction();

                // U1:
                var dialog = session1.createQuery(
                                "select d from Dialog d " +
                                        "join fetch d.dialogByMemberDetails dm " +
                                        "join fetch dm.member m " +
//                                    "join fetch m.memberInfo mi " +
                                        "where d.id = :id", Dialog.class)
                        .setParameter("id", 1)
                        .setLockMode(LockModeType.OPTIMISTIC)
                        .getSingleResult();
                dialog.setName(F_D_CH);

                // U2:
                var theSameDialog = session2.createQuery(
                                "select d from Dialog d " +
                                        "join fetch d.dialogByMemberDetails dm " +
                                        "join fetch dm.member m " +
//                                    "join fetch m.memberInfo mi " +
                                        "where d.id = :id", Dialog.class)
                        .setParameter("id", 1)
                        .setLockMode(LockModeType.OPTIMISTIC)
                        .getSingleResult();
                theSameDialog.setName(S_D_CH);

                // предролагаю что в рамках диалога апдейт не пройдет из-за последовательности каскадных операций,
                // но если прапдейтить того-же участника отдельно - изменения попадут в БД, т.к. у него нет версии..
                var firstMember = theSameDialog.getMembers().get(0);
                firstMember.setProfileId("Changed ProfileId ..");

                // Thanks to Optimistic Locking, First Commit will win.
                // Changes from second session will be ignored and app will throw ex because of that :
                session1.getTransaction().commit();
                session2.getTransaction().commit();
            }
        });
//        assertTrue(ex.getCause() instanceof OptimisticLockException);
        var dialog = readDialogByDialogIdWithFetchedMembers((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);
        assertEquals(F_D_CH, dialog.getName());

        cleanDb((Session) entityManagerFactory.createEntityManager());
    }
}