package com.goorm.tricountapi.repository;

import com.goorm.tricountapi.model.Member;
import com.goorm.tricountapi.model.Settlement;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepository{
    private final JdbcTemplate jdbcTemplate;

    // 정산 추가
    @Override
    public Settlement create(String name) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("settlement").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));

        Settlement settlement = new Settlement();
        settlement.setId(key.longValue());
        settlement.setName(name);

        return settlement;
    }

    // 정산 참가자(중간 테이블) 추가
    @Override
    public void addParticipantToSettlement(Long settlementId, Long memberId) {
        jdbcTemplate.update("INSERT INTO settlement_participant (settlement_id, member_id) VALUES (?, ?)", settlementId, memberId);
    }

    // 특정 정산 조회(정산 + 정산 참가자 자세한 정보)
    @Override
    public Optional<Settlement> findById(Long id) {
        List<Settlement> result = jdbcTemplate.query("SELECT * FROM SETTLEMENT\n" +
                "JOIN SETTLEMENT_PARTICIPANT ON SETTLEMENT.id = SETTLEMENT_PARTICIPANT.SETTLEMENT_ID\n" +
                "JOIN MEMBER ON SETTLEMENT_PARTICIPANT.MEMBER_ID = MEMBER.id\n" +
                "WHERE SETTLEMENT.id = ?", settlementParticipantRowMapper(), id);
        return result.stream().findAny();
    }

    // TODO 특정 멤버의 모든 정산 목록 조회 (나중에 구현)
    @Override
    public List<Settlement> findAllByMemberId(Long memberId) {
        return null;
    }

    private RowMapper<Settlement> settlementParticipantRowMapper() {
        return ((rs, rowNum) -> {
            Settlement settlement = new Settlement();
            settlement.setId(rs.getLong("settlement.id"));
            settlement.setName(rs.getString("settlement.name"));

            List<Member> participants = new ArrayList<>();
            do {
                Member participant = new Member(
                        rs.getLong("member.id"),
                        rs.getString("member.login_id"),
                        rs.getString("member.name"),
                        rs.getString("member.password")
                );
                participants.add(participant);
            } while(rs.next());

            settlement.setParticipants(participants);
            return settlement;
        });
    }
}
