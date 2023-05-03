package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface MessageRepository extends JpaRepository<Message, Integer>, ExtendedMessageRepository {

    @Query( value = "select m from Message m " +
                    "join fetch m.author a " +
                    "join fetch a.memberInfo m_inf " +
                    "join m.dialog d " +
                    "where d.dialogId = :dialogId or d.name = :dialogName " +
                    "order by  m.createAt",
            countQuery = "select count(*) from Message m " +
                    "join m.author a " +
                    "join a.memberInfo m_inf " +
                    "join m.dialog d " +
                    "where d.dialogId = :dialogId or d.name = :dialogName "
    )
    Page<Message> findAllByDialogIdOrName(PageRequest request, String dialogId, @Nullable String dialogName);
}