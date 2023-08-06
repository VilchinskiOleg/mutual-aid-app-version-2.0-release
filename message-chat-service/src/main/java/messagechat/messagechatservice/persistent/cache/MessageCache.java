package messagechat.messagechatservice.persistent.cache;

import org.springframework.data.repository.CrudRepository;

public interface MessageCache extends CrudRepository<CachedMessage, String>, ExtendedMessageCache {

}