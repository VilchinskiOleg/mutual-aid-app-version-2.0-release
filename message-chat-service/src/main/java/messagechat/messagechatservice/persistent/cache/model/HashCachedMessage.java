package messagechat.messagechatservice.persistent.cache.model;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

/**
 * Entity definition for saving to Redis DB as a Hash.
 *
 * New entity will be saved to Redis Hash with the key value: @RedisHash.value + ':' + @id
 */
@RedisHash("HashCachedMessage")

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HashCachedMessage implements Serializable, Comparable<HashCachedMessage> {

    public HashCachedMessage(Integer serialNumberDesc) {
        this.serialNumberDesc = serialNumberDesc;
    }


    // String value which should be (MANUALLY !) created by pattern: DialogID/Lang/SerialNumber/MessageID :
    @Id
    private String id;
    // ID of message defined in Domain layer :
    @Indexed
    private String internalId;
    private Integer serialNumberDesc;

    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long ttl;

    @Indexed
    private String lang;

    // ID of message defined in Domain layer :
    @Indexed
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
    public int compareTo(@NotNull HashCachedMessage o) {
        return serialNumberDesc - o.serialNumberDesc;
    }

    /**
     * Convert HashCachedMessage instance to a Map representation with all its properties.
     *
     * @return - Map of HashCachedMessage's properties
     */
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