package messagechat.messagechatservice.persistent.cache.repository;

import messagechat.messagechatservice.persistent.cache.CachedMessage;

import java.util.NavigableSet;

public interface ExtendedCachedMessageRepository {

    void saveMessageByKey(String key, CachedMessage message);

    NavigableSet<CachedMessage> getMessagesByKeyPattern(String pattern);

    Long removeMessagesByKeyPattern(String pattern);

    Boolean removeMessageByKeyPattern(String pattern);
}