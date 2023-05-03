package messagechat.messagechatservice.persistent.repository;


import messagechat.messagechatservice.persistent.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    @Query("select m from Member m where m.profileId = :profileId")
    Optional<Member> findByProfileId(String profileId);
}