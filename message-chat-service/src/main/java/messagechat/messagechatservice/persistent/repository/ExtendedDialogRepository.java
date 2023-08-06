package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Dialog;
import org.hibernate.Session;

import java.util.Optional;

public interface ExtendedDialogRepository {

    /**
     * But Hibernate by default runs his methods (find, get ..) with 'OptimisticLock' LockMode inside.
     * So that, it isn't necessary to intrude that property apparently in your request unless you want change that value
     * to other (PESSIMISTIC for example, e.t.c.).
     *
     * @param dialogId - id of Dialog to find.
     * @return dialog wrapper.
     */
    Optional<Dialog> findByDialogIdWithOptimisticLock(String dialogId);

//    Page<Dialog> findAllByMemberId(String memberId, PageRequest request);

    Session getHibernateSession();
}