package messagechat.messagechatservice.data.repository;

import lombok.Cleanup;
import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.data.common.AbstractTest;
import messagechat.messagechatservice.persistent.entity.Message;
import messagechat.messagechatservice.persistent.repository.ExtendedMessageRepositoryImpl;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.PageRequest.of;

@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MessageChatJpaConfig.class, ExtendedMessageRepositoryImpl.class})
public class MessageRepositoryTest extends AbstractTest {

    private static final String DIALOG_ID = "test-dialog-1";

    @Resource
    private EntityManagerFactory entityManagerFactory;
    @Resource
    private MessageRepository messageRepository;

    /**
     * Check that:
     * 1. Annotation @Query in method 'findAllByDialogId' works properly.
     * 2. Pagination in method 'findAllByDialogId' works properly.
     */
    @Test
    void get_all_messages_by_dialogId_or_dialogName_test() {
        @Cleanup var session = (Session) entityManagerFactory.createEntityManager();
        createDialogForCouple(session, DIALOG_ID);

        List<Message> messages1 = messageRepository.findAllByDialogIdOrName(
                of(0, 2),
                DIALOG_ID,
                "wrong_value").getContent();
        assertEquals(2, messages1.size());

        List<Message> messages2 = messageRepository.findAllByDialogIdOrName(
                of(0, 1),
                DIALOG_ID,
                null).getContent();
        assertEquals(1, messages2.size());

        cleanDb(session);
    }

    @Test
    void get_all_messages_by_dialogId_test() {
        @Cleanup var session = (Session) entityManagerFactory.createEntityManager();
        createDialogForCouple(session, DIALOG_ID);

        List<Message> messages1 = messageRepository.findAllByDialogId(
                of(0, 2),
                DIALOG_ID);
        assertEquals(2, messages1.size());

        List<Message> messages2 = messageRepository.findAllByDialogId(
                of(1, 1),
                DIALOG_ID);
        assertEquals(1, messages2.size());
        assertEquals(2, messages2.get(0).getId());

        cleanDb(session);
    }
}