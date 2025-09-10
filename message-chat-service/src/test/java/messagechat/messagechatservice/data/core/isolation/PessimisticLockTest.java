package messagechat.messagechatservice.data.core.isolation;

import lombok.Cleanup;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.data.common.AbstractTest;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.Member;
import org.hibernate.PessimisticLockException;
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

@Disabled(
        "Tests work separately, but don't work in a row, because of the same DB rows are locked by previous tests. " +
        "Sort it out and remove this annotation.")

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MessageChatJpaConfig.class, MessageChatConfigProps.class})
@MockBean(CacheManagerImpl.class) // in order to run a ctx
public class PessimisticLockTest extends AbstractTest {

    private static final String DIALOG_ID = "test-dialog-1";
    private static final String MESSAGE_ID = "test-message-1";

    private static final String F_D_CH = "Dialog Name - First update";
    private static final String S_D_CH = "Dialog Name - Second update";

    @Resource
    private EntityManagerFactory entityManagerFactory;


    /**
     * Block particular DB row for UPDATE, but allow to READ across different transactions
     * if they aren't going to chang it next, using PESSIMISTIC_READ.
     */
    @Test
    void lockRow_forUpdate_usingPessimisticReadLock() {
        createDialogForCouple((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);

        @Cleanup final Session session1 = (Session) entityManagerFactory.createEntityManager();
        @Cleanup final Session session2 = (Session) entityManagerFactory.createEntityManager();

        //validate:
        Exception ex = assertThrows(RollbackException.class, () -> {
            session1.beginTransaction();
            session2.beginTransaction();

            // U1 :
            var dialog = session1.find(Dialog.class, 1, LockModeType.PESSIMISTIC_READ);
            dialog.setName(F_D_CH);

            session2.createNativeQuery("SET lock_timeout = '5000ms'").executeUpdate();
            // U2 (будет заблокирован только для внесения изменений).
            // Использую "SET lock_timeout" выше, потому что Postgres не восприимчив к LockOptions,
            // "properties" параметер ниже предствален просто в качестве примера использования :
            var theSamedialog = session2.find(Dialog.class, 1, LockModeType.PESSIMISTIC_READ,
                    Map.of(JPA_LOCK_TIMEOUT, 5000));
            theSamedialog.setName("Second dialog NAME changing");

            // Исключение по таймауту будет выбрашено после следующей строчки кода
            // (во время попытки применить изменения) :
            session2.getTransaction().commit();
            session1.getTransaction().commit();
        });

        assertTrue(ex.getCause() instanceof javax.persistence.PessimisticLockException);
        var dialog = readDialogByDialogIdWithFetchedMembers((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);
        assertEquals("name of test-dialog-1", dialog.getName());
    }

    /**
     * Block particular DB row for UPDATE and READ, using PESSIMISTIC_WRITE.
     *
     * No inner Entities.
     */
    @Test
    void lockRow_forShareAndUpdate_usingPessimisticWriteLock() {
        createDialogForCouple((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);

        @Cleanup final Session session1 = (Session) entityManagerFactory.createEntityManager();
        @Cleanup final Session session2 = (Session) entityManagerFactory.createEntityManager();

        //validate:
        Exception ex = assertThrows(LockTimeoutException.class, () -> {
            session1.beginTransaction();
            session2.beginTransaction();

            // U1 :
            var dialog = session1.find(Dialog.class, 1, LockModeType.PESSIMISTIC_WRITE);
            dialog.setName(F_D_CH);

            // U2 (будет заблокирован даже для чтения) :
            session2.createNativeQuery("SET lock_timeout = '5000ms'").executeUpdate();
            // Запрос ниже пройдет успешно, т.к. участники диалога не запрашивались как вложеные сущности
            // и на эти записи в БД не распространилось влияние блокировки :
            var user1 = session2.find(Member.class, 1, LockModeType.PESSIMISTIC_READ);
            // Исключение по таймауту будет выбрашено после следующей строчки кода :
            var theSamedialog = session2.find(Dialog.class, 1, LockModeType.PESSIMISTIC_READ,
                    Map.of(JPA_LOCK_TIMEOUT, 5000));
            theSamedialog.setName("Second dialog NAME changing");

            session2.getTransaction().commit();
            session1.getTransaction().commit();
        });

        assertTrue(ex.getCause() instanceof PessimisticLockException);
        var dialog = readDialogByDialogIdWithFetchedMembers((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);
        assertEquals("name of test-dialog-1", dialog.getName());
    }

    /**
     * Block particular DB row for UPDATE and READ, using PESSIMISTIC_WRITE.
     *
     * Fetch inner Entities. As we can see inner Entities will be blocked as well.
     */
    @Test
    void lockRow_forShareAndUpdate_usingPessimisticWriteLock_withFetchingInnerEntities() {
        createDialogForCouple((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);

        @Cleanup final Session session1 = (Session) entityManagerFactory.createEntityManager();
        @Cleanup final Session session2 = (Session) entityManagerFactory.createEntityManager();

        //validate:
        Exception ex = assertThrows(javax.persistence.PessimisticLockException.class, () -> {
            session1.beginTransaction();
            session2.beginTransaction();

            // U1 :
            var dialog = session1.createQuery(
                            "select d from Dialog d " +
                                    "join fetch d.dialogByMemberDetails dm " +
                                    "join fetch dm.member m " +
                                    "where d.id = :id", Dialog.class)
                    .setParameter("id", 1)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult();

            // U2 (будет заблокирован даже для чтения) :
            session2.createNativeQuery("SET lock_timeout = '5000ms'").executeUpdate();
            // Исключение по таймауту будет выбрашено после следующей строчки кода,
            // т.к. в данном случае лок распространяется и на участников диалога
            // ( U1:    select .. join .. for update of
            //              dialogbyme1_,
            //              dialog0_,
            //              member2_
            // ):
            var member1 = session2.find(Member.class, 1, LockModeType.PESSIMISTIC_READ);
            var theSamedialog = session2.find(Dialog.class, 1, LockModeType.PESSIMISTIC_READ,
                    Map.of(JPA_LOCK_TIMEOUT, 5000));
            theSamedialog.setName("Second dialog NAME changing");

            session2.getTransaction().commit();
            session1.getTransaction().commit();
        });

        assertTrue(ex.getCause() instanceof PessimisticLockException);
        var dialog = readDialogByDialogIdWithFetchedMembers((Session) entityManagerFactory.createEntityManager(), DIALOG_ID);
        assertEquals("name of test-dialog-1", dialog.getName());
    }





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

    //            LockOptions lockOptions = new LockOptions(LockMode.PESSIMISTIC_WRITE);
    //            lockOptions.setTimeOut(1000);
    //            Message message = session1.get(Message.class, 1, lockOptions);
    //
    //            message.setDescription(F_D_CH);
    //
    //
    //

        //User 2:
    //            Message theSameMessage = session2.createQuery(
    //                            "select m from Message m " +
    //                                    "join fetch m.author a " +
    //                                    "join fetch a.memberInfo m_inf " +
    //                                    "where m.messageId = :messageId ", Message.class)
    //                    .setParameter("messageId", MESSAGE_ID)
    //                    .getSingleResult();
    //            theSameMessage.setDescription("Second message DESCRIPTION changing");
}