package messagechat.messagechatservice.persistent.repository;

import static messagechat.messagechatservice.util.Constant.Service.Mongo.MEMBERS;
import static messagechat.messagechatservice.util.Constant.Service.Mongo.PROFILE_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.support.PageableExecutionUtils.getPage;

import messagechat.messagechatservice.persistent.entity.Dialog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import javax.annotation.Resource;
import java.util.List;

public class ExtendedDialogRepositoryImpl implements ExtendedDialogRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Dialog> findAllByMemberId(String memberId, PageRequest pageRequest) {
        Criteria subCriteria = where(PROFILE_ID).is(memberId);
        Query query = new Query(where(MEMBERS).elemMatch(subCriteria));
        List<Dialog> dialogs = mongoTemplate.find(query.with(pageRequest), Dialog.class);
        return getPage(dialogs,
                       pageRequest,
                       () -> mongoTemplate.count(query.with(pageRequest).skip(-1).limit(-1), Dialog.class));
    }
}