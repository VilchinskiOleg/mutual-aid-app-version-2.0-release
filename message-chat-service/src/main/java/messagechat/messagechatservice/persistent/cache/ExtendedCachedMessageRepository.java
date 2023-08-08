package messagechat.messagechatservice.persistent.cache;

import java.util.NavigableSet;

public interface ExtendedCachedMessageRepository {

    NavigableSet<CachedMessage> getMessagesByKeyPattern(String pattern);

    Long removeMessagesByKeyPattern(String pattern);

    Boolean removeMessageByKeyPattern(String pattern);
}