package messagechat.messagechatservice.persistent.cache;

import java.util.Set;

public interface ExtendedMessageCache {

    Set<CachedMessage> getCachedMessagesByPattern(String pattern);
}