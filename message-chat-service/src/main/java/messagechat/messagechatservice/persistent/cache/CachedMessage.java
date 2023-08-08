package messagechat.messagechatservice.persistent.cache;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash("CachedMessage")
@NoArgsConstructor
public class CachedMessage implements Serializable, Comparable<CachedMessage> {

    private Integer id;
    private String internalId;
    private Integer serialNumberDesc;

    private String dialogId;
    private String authorId;

    private String authorNickName;
    private String description;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    @Override
    public int compareTo(@NotNull CachedMessage o) {
        return serialNumberDesc - o.serialNumberDesc;
    }

    public CachedMessage(Integer serialNumberDesc) {
        this.serialNumberDesc = serialNumberDesc;
    }
}