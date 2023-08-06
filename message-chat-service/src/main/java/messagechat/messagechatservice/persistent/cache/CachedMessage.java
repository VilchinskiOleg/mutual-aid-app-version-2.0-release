package messagechat.messagechatservice.persistent.cache;

import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("CachedMessage")
public class CachedMessage {

    private Integer id;
    private String internalId;

    private String dialogId;
    private String authorId;

    private String authorNickName;
    private String description;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}