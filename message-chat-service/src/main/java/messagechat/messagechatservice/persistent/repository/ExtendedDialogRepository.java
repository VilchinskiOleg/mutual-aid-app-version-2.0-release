package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Dialog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ExtendedDialogRepository {

    Page<Dialog> findAllByMemberId(String memberId, PageRequest request);
}