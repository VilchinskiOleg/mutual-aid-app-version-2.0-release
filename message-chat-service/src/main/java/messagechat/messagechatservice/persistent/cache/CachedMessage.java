package messagechat.messagechatservice.persistent.cache;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

// I don't use it for my logic (I convert entity to hash manually),
// but if I used Redis Repository entity mapping by @RedisHash -
// new entity would be saved to Redis with the key like: @RedisHash.value + ':' + @id):
@RedisHash("CachedMessage")

// Not necessary for Redis, only for my custom manipulation:
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CachedMessage implements Serializable, Comparable<CachedMessage> {

    @Id
    private Integer id;
    private String internalId;
    private Integer serialNumberDesc;

    private String dialogId;
    private String authorId;

    private String authorNickName;
    private String description;

    /**
     * Date when original Message was created/modified , not CachedMessage:
     */
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;


    @Override
    public int compareTo(@NotNull CachedMessage o) {
        return serialNumberDesc - o.serialNumberDesc;
    }

    public CachedMessage(Integer serialNumberDesc) {
        this.serialNumberDesc = serialNumberDesc;
    }

    public Map<Object,Object> getMap() {
        Map<Object,Object> result = new HashMap<>();
        try {
            for(var field : this.getClass().getDeclaredFields()){
                Object val = field.get(this);
                if (isNull(val)) continue;
                result.put(field.getName(), val);
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }
}