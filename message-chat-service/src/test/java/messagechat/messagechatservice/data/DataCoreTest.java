package messagechat.messagechatservice.data;

import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.Member;
import messagechat.messagechatservice.persistent.entity.Message;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MessageChatJpaConfig.class})
public class DataCoreTest extends AbstractTest {

    private static final String DIALOG_ID = "test-dialog-1";


    @Resource
    private EntityManagerFactory entityManagerFactory;
    private Session hibernateEntityManagerImpl;


    @PostConstruct
    public void initEntityManager() {
        this.hibernateEntityManagerImpl = (Session) entityManagerFactory.createEntityManager();
    }


    @Test
    void save_and_read_dialog_test() {
        createDialog(hibernateEntityManagerImpl,"test-dialog-1");

        Dialog savedDialog = hibernateEntityManagerImpl.createQuery("from Dialog d where d.dialogId = :dialogId", Dialog.class)
                .setParameter("dialogId", "test-dialog-1")
                .getSingleResult();
        //validate:
        assertNotNull(savedDialog);

        cleanDb(hibernateEntityManagerImpl);
    }

    @Test
    void add_new_message_to_dialog_and_Read_updated_dialog_Retrieve_author_and_existed_dialog_from_db() {
        createDialog(hibernateEntityManagerImpl, "test-dialog-1");

        Dialog savedDialog =  hibernateEntityManagerImpl.createQuery("from Dialog d where d.dialogId = :dialogId", Dialog.class)
                .setParameter("dialogId", "test-dialog-1")
                .getSingleResult();
        //validate:
        assertEquals(2, savedDialog.getMessages().size());
        //you have to retrieve real Member (from proxy) value before cleaning Session and PC:
        var randomAuthor = savedDialog.getMembers().get(0);
        //[Warning!]:
        // You must clear Persistence Context (PC) in order to detach current Dialog and go to the DB during reading updated version.
        // Otherwise, you will be provided by old (savedDialog) Dialog from PC, which contain only 2 messages:
        hibernateEntityManagerImpl.clear();

        var transaction = hibernateEntityManagerImpl.beginTransaction();
        try {
            var msg3 = Message.builder()
                    .messageId("test-message-3")
                    .description("description of test-message-3")
                    .createAt(now()).build();
            msg3.setDialog(savedDialog);
            msg3.setAuthor(randomAuthor);

            //save message ([!]use methods):
            hibernateEntityManagerImpl.persist(msg3);
            transaction.commit();
            //[Warning!]:
            // You should clear PC before next reading expected updated Dialog.
            // Otherwise, you will be provided by correct updated version of Dialog from DB, but
            // new Message #3 will be obtained from PC and will contain only different references
            // to Author and Dialog (that Objects will be refreshed from DB by new actual data) then other messages:
            hibernateEntityManagerImpl.clear();
        } catch (Exception ex) {
            transaction.rollback();
        }

        Dialog updatedDialog = hibernateEntityManagerImpl.createQuery("from Dialog d where d.dialogId = :dialogId", Dialog.class)
                .setParameter("dialogId", "test-dialog-1")
                .getSingleResult();
        //validate:
        assertEquals(3, updatedDialog.getMessages().size());
        //[Warning!]:
        // Clear PC only after using lazy initialization ('updatedDialog.getMessages().size()' in our case):
        hibernateEntityManagerImpl.clear();

        cleanDb(hibernateEntityManagerImpl);
    }

    @Test
    void add_new_message_to_dialog_and_Read_updated_dialog_Use_only_ids_of_author_and_existed_dialog() {
        createDialog(hibernateEntityManagerImpl, "test-dialog-1");
        Dialog dialog = readDialogByDialogIdWithFetchedMembers(hibernateEntityManagerImpl, "test-dialog-1");
        final Integer MOCK_RETRIEVED_AUTHOR_ID = dialog.getMembers().get(0).getId();
        final Integer MOCK_RETRIEVED_DIALOG_ID = dialog.getId();
        // If we use optimistic lock with OptimisticLockType = 'version', we will have to retrieve and add current version to (inner entity) Dialog.
        // Otherwise, Hibernate will consider that it's not a existed entity Dialog but new one.
        final Long MOCK_RETRIEVED_DIALOG_VERSION = dialog.getVersion();

        var transaction = hibernateEntityManagerImpl.beginTransaction();
        try {
            var savedDialog = Dialog.builder()
                    .version(MOCK_RETRIEVED_DIALOG_VERSION)
                    .id(MOCK_RETRIEVED_DIALOG_ID).build();
            var randomAuthor = new Member(MOCK_RETRIEVED_AUTHOR_ID);

            var msg3 = Message.builder()
                    .messageId("test-message-3")
                    .description("description of test-message-3")
                    .createAt(now()).build();
            msg3.setDialog(savedDialog);
            msg3.setAuthor(randomAuthor);

            //save message ([!]use methods):
            hibernateEntityManagerImpl.persist(msg3);
            transaction.commit();
            hibernateEntityManagerImpl.clear();
        } catch (Exception ex) {
            transaction.rollback();
        }

        Dialog updatedDialog = hibernateEntityManagerImpl.createQuery("from Dialog d where d.dialogId = :dialogId", Dialog.class)
                .setParameter("dialogId", "test-dialog-1")
                .getSingleResult();
        //validate:
        assertEquals(3, updatedDialog.getMessages().size());
        //[Warning!]:
        // Clear PC only after using lazy initialization ('updatedDialog.getMessages().size()' in our case):
        hibernateEntityManagerImpl.clear();

        cleanDb(hibernateEntityManagerImpl);
    }

    @Test
    void add_new_member_to_dialog_and_read_updated_dialog() {

    }
}