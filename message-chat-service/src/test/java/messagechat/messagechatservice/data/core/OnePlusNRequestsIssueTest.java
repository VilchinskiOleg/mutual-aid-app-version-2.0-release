package messagechat.messagechatservice.data.core;

import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.MessageChatJpaConfig;
import messagechat.messagechatservice.data.common.AbstractTest;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.persistent.entity.Member;
import messagechat.messagechatservice.persistent.entity.Message;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MessageChatJpaConfig.class, MessageChatConfigProps.class})
public class OnePlusNRequestsIssueTest extends AbstractTest {

    private static final String DIALOG_ID = "test-dialog-1";

    // Just in order to run a context for test:
    @MockBean
    private CacheManagerImpl cacheManagerImpl;

    @Resource
    private EntityManagerFactory entityManagerFactory;
    private Session hibernateEntityManagerImpl;


    @PostConstruct
    public void initEntityManager() {
        this.hibernateEntityManagerImpl = (Session) entityManagerFactory.createEntityManager();
    }


    /**
     * First SQL request will fetch only Message entity. Other entities which have fetch type EAGER
     * will be fetched by additional SQL request to DB automatically. Lazy entities will be fetched by
     * the same way during their initialization (by additional SQL request).
     */
    @Test
    void get_all_messages_by_dialogId_query_Have_issue_with_additional_requests_to_db_Test() {
        createDialogForCouple(hibernateEntityManagerImpl, DIALOG_ID);

        List<Message> messages = hibernateEntityManagerImpl.createQuery(
                        "select m from Message m " +
                                "join m.dialog d " +
                                "where d.dialogId = :dialogId " +
                                "order by  m.createAt", Message.class)
                .setParameter("dialogId",DIALOG_ID)
                .getResultList();
        messages.forEach(message -> System.out.println(message.getAuthor().getMemberInfo().getNickName()));
        hibernateEntityManagerImpl.clear();

        //validate:
        assertEquals(2, messages.size());
        //TODO: how to validate amount of SQL request? COUNT "SELECT" WORDS FROM LOG FILE.

        cleanDb(hibernateEntityManagerImpl);
    }

    /**
     * First SQL request will fetch Message, Member, MemberInfo entities.
     * So, all marked entities will be fetched by only one SQL request to DB.
     *
     * Thanks to @MeanyToOne relation Message-Member(author) we will not be provided by 'cartesian product'
     * and, as result, we can use pagination for request like this.
     *
     * But also fetching entities by that way will break Lazy initialisation (Proxy) for them.
     * Marked entities will be initialized even if they are Lazy.
     */
    @Test
    void get_all_messages_by_dialogId_query_Fix_issue_with_additional_requests_to_db_by_fetch_Test() {
        createDialogForCouple(hibernateEntityManagerImpl, DIALOG_ID);

        List<Message> messages = hibernateEntityManagerImpl.createQuery(
                        "select m from Message m " +
                                "join fetch m.author a " +
                                "join fetch a.memberInfo m_inf " +
                                "join m.dialog d " +
                                "where d.dialogId = :dialogId " +
                                "order by  m.createAt", Message.class)
                .setParameter("dialogId",DIALOG_ID)
                .getResultList();
        messages.forEach(message -> System.out.println(message.getAuthor().getMemberInfo().getNickName()));
        hibernateEntityManagerImpl.clear();

        //validate:
        assertEquals(2, messages.size());
        //TODO: how to validate amount of SQL request?

        cleanDb(hibernateEntityManagerImpl);
    }

    /**
     * First SQL request will fetch Message, Member, MemberInfo entities.
     * So, all marked in a Graph entities will be fetched by only one SQL request to DB
     * like previous Test as well.
     */
    @Test
    void get_all_messages_by_dialogId_query_Fix_issue_with_additional_requests_to_db_by_entity_graph_Test() {
        createDialogForCouple(hibernateEntityManagerImpl, DIALOG_ID);

        RootGraph<Message> messageGraph = hibernateEntityManagerImpl.createEntityGraph(Message.class);
        messageGraph.addAttributeNodes("author");
        SubGraph<Member> memberSubGraph = messageGraph.addSubgraph("author", Member.class);
        memberSubGraph.addAttributeNodes("memberInfo");

        List<Message> messages = hibernateEntityManagerImpl.createQuery(
                        "select m from Message m " +
                                "join m.dialog d " +
                                "where d.dialogId = :dialogId " +
                                "order by  m.createAt", Message.class)
                .setParameter("dialogId",DIALOG_ID)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), messageGraph)
                .getResultList();
        messages.forEach(message -> System.out.println(message.getAuthor().getMemberInfo().getNickName()));
        hibernateEntityManagerImpl.clear();

        //validate:
        assertEquals(2, messages.size());
        //TODO: how to validate amount of SQL request?

        cleanDb(hibernateEntityManagerImpl);
    }
}