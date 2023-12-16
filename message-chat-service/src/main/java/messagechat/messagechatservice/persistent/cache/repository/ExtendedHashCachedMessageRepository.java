package messagechat.messagechatservice.persistent.cache.repository;

import messagechat.messagechatservice.persistent.cache.model.HashCachedMessage;

import java.util.NavigableSet;

public interface ExtendedHashCachedMessageRepository {

    /**
     * Save Message to Redis Hash by key.
     *
     * @param key - key for new Hash.
     * @param message - payload for new Hash.
     */
    void saveMessageByKey(String key, HashCachedMessage message);

    /**
     * Retrieve all Messages from the cache that match the provided key-pattern.
     *
     * @param pattern - provided key-pattern
     * @return - [1.] cached Messages or [2.] null if no one Message matches the key-pattern
     */
    NavigableSet<HashCachedMessage> getMessagesByKeyPattern(String pattern);

    /**
     * Try to remove all Messages that match the provided key-pattern.
     *
     * @param pattern - provided key-pattern
     * @return - amount of deleted records from Redis Hash
     */
    Long removeMessagesByKeyPattern(String pattern);

    /**
     * Try to remove Message by Message ID provided within key-pattern.
     *
     * @param pattern - provided key-pattern with contains Message ID
     * @return - if Message was deleted or not
     */
    Boolean removeMessageByKeyPattern(String pattern);
}
