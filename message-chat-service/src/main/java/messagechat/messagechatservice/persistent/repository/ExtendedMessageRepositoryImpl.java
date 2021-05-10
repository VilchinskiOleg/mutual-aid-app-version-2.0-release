package messagechat.messagechatservice.persistent.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.support.PageableExecutionUtils.getPage;

import messagechat.messagechatservice.persistent.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import javax.annotation.Resource;

public class ExtendedMessageRepositoryImpl implements ExtendedMessageRepository {

    @Resource
    private MongoTemplate template;

    @Override
    public Page<Message> findAllByDialogId(PageRequest pageRequest, String dialogId) {
        Query query = new Query();
        query.addCriteria(where("dialogId").is(dialogId));
        query.with(pageRequest);
        var messages = template.find(query, Message.class);
        return getPage(messages,
                       pageRequest,
                       () -> template.count(query.skip(-1).limit(-1), Message.class));
    }
}
