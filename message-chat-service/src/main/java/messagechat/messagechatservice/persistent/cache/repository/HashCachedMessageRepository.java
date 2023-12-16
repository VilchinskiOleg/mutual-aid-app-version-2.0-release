package messagechat.messagechatservice.persistent.cache.repository;

import messagechat.messagechatservice.persistent.cache.model.HashCachedMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.NavigableSet;

public interface HashCachedMessageRepository extends CrudRepository<HashCachedMessage, String>, ExtendedHashCachedMessageRepository {

    NavigableSet<HashCachedMessage> findAllByDialogIdAndLang(String dialogId, String lang);
}