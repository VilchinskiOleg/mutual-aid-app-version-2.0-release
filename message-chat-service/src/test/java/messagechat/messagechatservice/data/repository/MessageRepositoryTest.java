package messagechat.messagechatservice.data.repository;

import lombok.Cleanup;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.data.common.AbstractTest;
import messagechat.messagechatservice.domain.service.proessor.ExternalCacheManager;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.Message;
import messagechat.messagechatservice.persistent.repository.ExtendedMessageRepositoryImpl;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.hibernate.Session;
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
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.PageRequest.of;

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        MessageChatJpaConfig.class, ExtendedMessageRepositoryImpl.class, MessageChatConfigProps.class
})
public class MessageRepositoryTest extends AbstractTest {

    private static final String DIALOG_ID = "test-dialog-1";

    @Resource
    private EntityManagerFactory entityManagerFactory;
    @Resource
    private MessageRepository messageRepository;

    // Just in order to run a context for test:
    @MockBean
    private ExternalCacheManager externalCacheManager;

    /**
     * Check that:
     * 1. Annotation @Query in method 'findAllByDialogId' works properly.
     * 2. Pagination in method 'findAllByDialogId' works properly.
     */
    @Test
    void get_all_messages_by_dialogId_or_dialogName_test() {
        @Cleanup var session = (Session) entityManagerFactory.createEntityManager();
        createDialogForCouple(session, DIALOG_ID);

        // case 1:
        List<Message> messages1 = messageRepository.findAllByDialogIdOrName(
                of(0, 2),
                DIALOG_ID,
                "wrong_value").getContent();
        assertEquals(2, messages1.size());

        // case 2:
        List<Message> messages2 = messageRepository.findAllByDialogIdOrName(
                of(0, 1),
                DIALOG_ID,
                null).getContent();
        assertEquals(1, messages2.size());

        cleanDb(session);
    }

    /**
     * Check reading of the messages by Criteria api.
     */
    @Test
    void get_all_messages_by_dialogId_test() {
        @Cleanup var session = (Session) entityManagerFactory.createEntityManager();
        Dialog createdDialog = createDialogForCouple(session, DIALOG_ID);

        // case 1:
        // Perform:
        List<Message> messages1 = messageRepository.findAllByDialogId(
                of(0, 2),
                DIALOG_ID);
        // Validate:
        assertEquals(2, messages1.size());

        // case 2:
        // Perform:
        List<Message> messages2 = messageRepository.findAllByDialogId(
                of(1, 1),
                DIALOG_ID);
        // Validate:
        assertEquals(1, messages2.size());
        NavigableSet<Message> orderedDialogMessages = new TreeSet<>(Comparator.comparingInt(Message::getId));
        orderedDialogMessages.addAll(createdDialog.getMessages());
        assertEquals(orderedDialogMessages.last().getId(), messages2.get(0).getId());

        cleanDb(session);
    }
}