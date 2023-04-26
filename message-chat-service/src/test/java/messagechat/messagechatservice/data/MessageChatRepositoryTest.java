package messagechat.messagechatservice.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Cleanup;
import lombok.NonNull;
import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.Member;
import messagechat.messagechatservice.persistent.entity.MemberInfo;
import messagechat.messagechatservice.persistent.entity.Message;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.PageRequest.of;

@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MessageChatJpaConfig.class})
public class MessageChatRepositoryTest extends DatabaseSourceTestConfig {

    private static final String DIALOG_ID = "test-dialog-1";
    private static final String MOCK_ENTITY_URL = "data/";
    private static final ObjectMapper READER = new ObjectMapper().registerModule(new JavaTimeModule());;

    @Resource
    private DialogRepository dialogRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private EntityManagerFactory entityManagerFactory;
    private Session hibernateEntityManagerImpl;

    @PostConstruct
    public void initEntityManager() {
        this.hibernateEntityManagerImpl = (Session) entityManagerFactory.createEntityManager();
    }

    @BeforeAll
    static void initAll() {

    }

    @BeforeEach
    protected void initEach() {
        showTestDbUrl();
    }

    @Test
    void save_and_read_dialog_test() {
        createDialog("test-dialog-1");

        Dialog savedDialog = readDialogByDialogId("test-dialog-1");
        //validate:
        assertNotNull(savedDialog);

        cleanDb();
    }

    /**
     * First SQL request will fetch only Message entity. Other entities which have fetch type EAGER
     * will be fetched by additional SQL request to DB automatically. Lazy entities will be fetched by
     * the same way during their initialization.
     */
    @Test
    void get_all_messages_by_dialogId_query_Have_issue_with_additional_requests_to_db_Test() {
        createDialog("test-dialog-1");

        List<Message> messages = hibernateEntityManagerImpl.createQuery(
                "select m from Message m " +
                        "join m.dialog d " +
                        "where d.dialogId = :dialogId " +
                        "order by  m.createAt", Message.class)
                .setParameter("dialogId",DIALOG_ID)
                .getResultList();
        hibernateEntityManagerImpl.clear();

        //validate:
        assertEquals(2, messages.size());

        cleanDb();
    }

    /**
     * First SQL request will fetch Message, Member, MemberInfo entities.
     * So, all EAGER entities will be fetched by only one SQL request to DB.
     */
    @Test
    void get_all_messages_by_dialogId_query_Fix_issue_with_additional_requests_to_db_by_fetch_Test() {
        createDialog("test-dialog-1");

        List<Message> messages = hibernateEntityManagerImpl.createQuery(
                        "select m from Message m " +
                                "join fetch m.author a " +
                                "join fetch a.memberInfo m_inf " +
                                "join m.dialog d " +
                                "where d.dialogId = :dialogId " +
                                "order by  m.createAt", Message.class)
                .setParameter("dialogId",DIALOG_ID)
                .getResultList();
        hibernateEntityManagerImpl.clear();

        //validate:
        assertEquals(2, messages.size());

        cleanDb();
    }

    @Test
    void add_new_message_to_dialog_and_Read_updated_dialog_Retrieve_author_and_existed_dialog_from_db() {
        createDialog("test-dialog-1");

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

        cleanDb();
    }

    @Test
    void add_new_message_to_dialog_and_Read_updated_dialog_Use_only_ids_of_author_and_existed_dialog() {
        createDialog("test-dialog-1");
        Dialog dialog = readDialogByDialogId("test-dialog-1");
        final Integer MOCK_RETRIEVED_AUTHOR_ID = dialog.getMembers().get(0).getId();
        final Integer MOCK_RETRIEVED_DIALOG_ID = dialog.getId();

        var transaction = hibernateEntityManagerImpl.beginTransaction();
        try {
            var savedDialog = Dialog.builder().id(MOCK_RETRIEVED_DIALOG_ID).build();
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

        cleanDb();
    }

    @Test
    void add_new_member_to_dialog_and_read_updated_dialog() {

    }

    /**
     * Check that:
     * 1. Annotation @Query in method 'findAllByDialogId' works properly.
     * 2. Pagination in method 'findAllByDialogId' works properly.
     */
    @Test
    void get_all_messages_by_dialogId_repository_test() {
        createDialog("test-dialog-1");

        List<Message> messages1 = messageRepository.findAllByDialogId(DIALOG_ID, of(0, 2)).getContent();
        assertEquals(2, messages1.size());

        List<Message> messages2 = messageRepository.findAllByDialogId(DIALOG_ID, of(0, 1)).getContent();
        assertEquals(1, messages2.size());

        cleanDb();
    }


    private void createDialog(String dialogId) {
        var transaction = hibernateEntityManagerImpl.beginTransaction();
        try {
            var usrInfo1 = MemberInfo.builder()
                    .firstName("firstname of test-user-1")
                    .lastName("lastname of test-user-1")
                    .nickName("nickname of test-user-1").build();
            var usr1 = new Member("test-user-1", usrInfo1);

            var usrInfo2 = MemberInfo.builder()
                    .firstName("firstname of test-user-2")
                    .lastName("lastname of test-user-2")
                    .nickName("nickname of test-user-2").build();
            var usr2 = new Member("test-user-2", usrInfo2);

            var dialog = Dialog.builder()
                    .dialogId(dialogId)
                    .name("name of " + dialogId)
                    .status("ACTIVE")
                    .type("FACE_TO_FACE_DIALOG")
                    .createAt(now())
                    .createByMemberId("creator of " + dialogId).build();
            dialog.addMember(usr1);
            dialog.addMember(usr2);

            var msg1 = Message.builder()
                    .messageId("test-message-1")
                    .description("description of test-message-1")
                    .createAt(now()).build();
            msg1.setDialog(dialog);
            msg1.setAuthor(usr1);

            Thread.sleep(2000);

            var msg2 = Message.builder()
                    .messageId("test-message-2")
                    .description("description of test-message-2")
                    .createAt(now()).build();
            msg2.setDialog(dialog);
            msg2.setAuthor(usr2);

            //save dialog ([!]use methods):
            hibernateEntityManagerImpl.persist(dialog);
            transaction.commit();
            hibernateEntityManagerImpl.clear();
        } catch (Exception ex) {
            transaction.rollback();
        }
    }

    private Dialog readDialogByDialogId(@NonNull String dialogId) {
        Dialog result = hibernateEntityManagerImpl.createQuery("from Dialog d where d.dialogId = :dialogId", Dialog.class)
                .setParameter("dialogId", dialogId)
                .getSingleResult();
        //todo: fix N+1 issue!
        hibernateEntityManagerImpl.clear();
        return result;
    }

    private void cleanDb() {
        var transaction = hibernateEntityManagerImpl.beginTransaction();
        try {
            //remove dialogs ([!]use methods):
            hibernateEntityManagerImpl.createQuery("select d from Dialog d", Dialog.class).getResultList()
                    .forEach(hibernateEntityManagerImpl::remove);
            //remove members ([!]use methods):
            hibernateEntityManagerImpl.createQuery("select m from Member m", Member.class).getResultList()
                    .forEach(hibernateEntityManagerImpl::remove);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }
    }


    private <T> T readMockEntity(String urlPostfix, Class<T> clazz) throws IOException {
        @Cleanup var inStream = getClass().getClassLoader().getResourceAsStream(MOCK_ENTITY_URL.concat(urlPostfix));
        return READER.readValue(inStream, clazz);
    }
}