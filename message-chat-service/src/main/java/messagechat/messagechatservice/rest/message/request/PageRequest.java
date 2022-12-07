package messagechat.messagechatservice.rest.message.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {

    @NonNull
    private Integer pageNumber;
    @NonNull
    private Integer size;
}