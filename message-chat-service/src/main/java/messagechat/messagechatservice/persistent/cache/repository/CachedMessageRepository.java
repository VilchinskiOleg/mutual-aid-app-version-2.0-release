package messagechat.messagechatservice.persistent.cache.repository;

import messagechat.messagechatservice.persistent.cache.CachedMessage;
import org.springframework.data.repository.CrudRepository;

public interface CachedMessageRepository extends CrudRepository<CachedMessage, String>, ExtendedCachedMessageRepository {

}