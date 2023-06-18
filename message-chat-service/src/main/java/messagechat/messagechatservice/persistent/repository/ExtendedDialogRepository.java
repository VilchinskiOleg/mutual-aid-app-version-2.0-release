package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Dialog;

import java.util.Optional;

public interface ExtendedDialogRepository {

    Optional<Dialog> findByDialogIdWithOptimisticLock(String dialogId);

//    Page<Dialog> findAllByMemberId(String memberId, PageRequest request);
}