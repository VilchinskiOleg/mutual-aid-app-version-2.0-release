package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Message;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Root;
import java.util.List;

public class ExtendedMessageRepositoryImpl implements ExtendedMessageRepository {

    @Resource
    private EntityManager entityManager;

    @Override
    public List<Message> findAllByDialogId(PageRequest page, String dialogId) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Message.class);
        Root<Message> message = query.from(Message.class);

        var dialog = message.join("dialog");
        var author = message.fetch("author");
        author.fetch("memberInfo");

        query.select(message).where(criteriaBuilder.equal(dialog.get("dialogId"), dialogId));
        return entityManager.createQuery(query)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
    }
}