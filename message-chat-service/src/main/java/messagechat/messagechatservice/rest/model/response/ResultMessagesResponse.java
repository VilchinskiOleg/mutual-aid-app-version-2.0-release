package messagechat.messagechatservice.rest.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import messagechat.messagechatservice.rest.model.ShirtMessage;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@Getter
@Setter
@Builder
public class ResultMessagesResponse {

    private CollectionModel<EntityModel<ShirtMessage>> shirtMessages;
    private Integer allPages;
    private Integer currentPage;
    private Integer sizeOfPage;
}
