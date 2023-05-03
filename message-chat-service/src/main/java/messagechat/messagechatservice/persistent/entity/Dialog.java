package messagechat.messagechatservice.persistent.entity;

import lombok.*;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"dialogByMemberDetails", "messages"})

@Entity
@Table(name = "dialog")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String dialogId;

    private String name;

    @Version
    private Long version;

    @ToString.Exclude
    @Builder.Default // Will initialize field by inline '= new ArrayList<>()' below if I use builder:
    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL)
    private List<DialogByMember> dialogByMemberDetails = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default // Will initialize field by inline '= new HashSet<>()' below if I use builder:
    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet<>();

    private String status;
    private String type;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private String createByMemberId;
    private String modifyByMemberId;


    /**
     * Don't use it in your client code. Add new message to Dialog by 'message.setDialog()'.
     * That method was created to call from related Message entity during initialization.
     *
     * @param message - related Message entity.
     */
    public void addMessage(@NonNull Message message) {
        messages.add(message);
    }

    /**
     * Can use to initialize Dialog-Entity relation from Dialog side.
     *
     * @param messages - list of related Message entities.
     */
    public void addMessages(@NotEmpty Set<Message> messages) {
        messages.forEach(message -> message.setDialog(this));
    }

    /**
     * Create new record DialogByMemberDetails with current Dialog ID and received Member automatically.
     * Use it to add new member to the conversation the most convenient way.
     *
     * @param member - new Member for conversation you want to add.
     */
    public void addMember(@NonNull Member member) {
        dialogByMemberDetails.add(new DialogByMember(this, member));
    }

    /**
     * Provide you with most convenient way to retrieve all Members to belong to current Dialog.
     *
     * @return - Members.
     */
    public List<Member> getMembers() {
        return dialogByMemberDetails.stream()
                .map(DialogByMember::getMember)
                .collect(toList());
    }
}