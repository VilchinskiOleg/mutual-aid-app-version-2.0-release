package messagechat.messagechatservice.rest.message.response;

import lombok.Getter;
import lombok.Setter;
import org.exception.handling.autoconfiguration.model.BaseResponse;

@Getter
@Setter
public abstract class PageResponse extends BaseResponse {

    private Integer currentPage;
    private Integer sizeOfPage;
    private Integer allPages;
}