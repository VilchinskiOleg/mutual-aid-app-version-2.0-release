package messagechat.messagechatservice.persistent.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String messageId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    private String description;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;


    /**
     * Initializes all relation Dialog-Message automatically.
     * Use that method from your client code for initialization.
     *
     * @param dialog - related Dialog entity.
     */
    public void setDialog(@NonNull Dialog dialog) {
        this.dialog = dialog;
        dialog.addMessage(this);
    }
}