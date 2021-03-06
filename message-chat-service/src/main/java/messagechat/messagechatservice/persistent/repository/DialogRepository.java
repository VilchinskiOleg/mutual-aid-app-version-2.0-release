package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Dialog;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface DialogRepository extends MongoRepository<Dialog, String>, ExtendedDialogRepository {

    Optional<Dialog> findByInternalId(String internalId);
}