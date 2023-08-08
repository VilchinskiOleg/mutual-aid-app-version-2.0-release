package messagechat.messagechatservice.persistent.cache;

import org.springframework.data.repository.CrudRepository;

public interface CachedMessageRepository extends CrudRepository<CachedMessage, String>, ExtendedCachedMessageRepository {

}