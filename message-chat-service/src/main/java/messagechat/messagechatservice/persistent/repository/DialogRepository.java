package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DialogRepository extends JpaRepository<Dialog, Integer>, ExtendedDialogRepository {

    @Query( "select d from Dialog d " +
            "join fetch d.dialogByMemberDetails d_by_m " +
            "join fetch d_by_m.member m " +
            "join fetch m.memberInfo m_inf " +
            "where d.dialogId = :dialogId")
    Optional<Dialog> findByDialogId(String dialogId);
}