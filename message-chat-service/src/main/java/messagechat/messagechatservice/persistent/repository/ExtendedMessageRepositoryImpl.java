package messagechat.messagechatservice.persistent.repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

public class ExtendedMessageRepositoryImpl implements ExtendedMessageRepository {

    @Resource
    private EntityManager entityManager;
}