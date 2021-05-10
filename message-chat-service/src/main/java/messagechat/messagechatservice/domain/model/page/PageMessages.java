package messagechat.messagechatservice.domain.model.page;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import messagechat.messagechatservice.domain.model.Message;
import java.util.List;

@Getter
@Setter
@Builder
public class PageMessages {

    private List<Message> messages;
    private Integer allPages;
    private Integer currentPage;
    private Integer sizeOfPage;
}
