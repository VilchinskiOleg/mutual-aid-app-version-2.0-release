package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MessageRepository extends MongoRepository<Message, String> {

    @Query(value = "{'dialogId': ?0}", sort = "{'createAt': 1}")
    Page<Message> findAllByDialogId(String dialogId, PageRequest request);
}
