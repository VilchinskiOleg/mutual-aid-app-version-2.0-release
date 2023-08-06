package messagechat.messagechatservice.rest.message.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

    @NonNull
    private Integer pageNumber;
    @NonNull
    private Integer size;
}