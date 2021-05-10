package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String>, ExtendedMessageRepository {

    /**
     * Update/create this method when I will be ready (use @Query):
     *
     * Page<Message> findAllByDialogId(PageRequest pageRequest, String dialogId);
     */
}
