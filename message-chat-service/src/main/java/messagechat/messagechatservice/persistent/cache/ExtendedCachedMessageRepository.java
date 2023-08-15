package messagechat.messagechatservice.persistent.cache;

import java.util.NavigableSet;

public interface ExtendedCachedMessageRepository {

    void saveMessageByKey(String key, CachedMessage message);

    NavigableSet<CachedMessage> getMessagesByKeyPattern(String pattern);

    Long removeMessagesByKeyPattern(String pattern);

    Boolean removeMessageByKeyPattern(String pattern);
}