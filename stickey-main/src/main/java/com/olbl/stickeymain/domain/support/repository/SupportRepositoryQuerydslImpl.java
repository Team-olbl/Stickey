package com.olbl.stickeymain.domain.support.repository;

import static com.olbl.stickeymain.domain.support.entity.QSupport.support;
import static com.olbl.stickeymain.domain.user.organization.entity.QOrganization.organization;
import static com.olbl.stickeymain.global.result.error.ErrorCode.SUPPORT_DO_NOT_EXISTS;

import com.olbl.stickeymain.domain.support.dto.SupportByItemRes;
import com.olbl.stickeymain.domain.support.dto.SupportListRes;
import com.olbl.stickeymain.domain.support.dto.SupportOneRes;
import com.olbl.stickeymain.domain.support.dto.SupportRes;
import com.olbl.stickeymain.domain.support.entity.SupportStatus;
import com.olbl.stickeymain.global.result.error.exception.BusinessException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SupportRepositoryQuerydslImpl implements SupportRepositoryQuerydsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SupportListRes getSupportListByFlag(Integer flag) {
        BooleanBuilder booleanBuilder = generateQueryCondition(flag);
        List<SupportRes> supportResList = jpaQueryFactory.select(Projections.fields(
                SupportRes.class,
                support.id.as("id"),
                support.title,
                support.content,
                support.startTime,
                support.endTime,
                support.supportImage,
                organization.name.as("organizationName"),
                organization.profileImage.as("profileImage")
            ))
            .from(support)
            .innerJoin(support.organization, organization)
            .where(booleanBuilder)
            .orderBy(support.endTime.asc())
            .fetch();

        return new SupportListRes(supportResList);
    }

    @Override
    public SupportOneRes getSupportOneById(int id) {
        SupportOneRes supportOneRes = jpaQueryFactory.select(Projections.fields(
                SupportOneRes.class,
                organization.id.as("organizationId"),
                support.title,
                support.content,
                support.supportImage,
                support.endTime,
                support.startTime,
                organization.profileImage,
                organization.name,
                organization.email,
                organization.phone,
                organization.address,
                organization.manager
            ))
            .from(support)
            .innerJoin(support.organization, organization)
            .where(support.id.eq(id))
            .fetchOne();

        if (supportOneRes == null) {
            throw new BusinessException(SUPPORT_DO_NOT_EXISTS);
        }

        return supportOneRes;
    }

    @Override
    public SupportByItemRes getSupportByItemByTime() {

        LocalDateTime now = LocalDateTime.now(); //현재 시간 기준

        SupportByItemRes itemRes = jpaQueryFactory.select(
                Projections.fields(SupportByItemRes.class,
                    support.id.as("id"),
                    support.organization.name.as("name")
                )).from(support)
            .where(support.startTime.before(now)
                .and(support.endTime.after(now))
                .and(support.status.eq(SupportStatus.ACCEPTED)))
            .orderBy(support.endTime.asc())
            .fetchFirst();

        if (null == itemRes) {
            itemRes = new SupportByItemRes();
            itemRes.setId(0);
            itemRes.setName(null);
        }

        return itemRes;
    }

    private BooleanBuilder generateQueryCondition(Integer flag) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (flag == 0) { // 진행 중인 후원글
            booleanBuilder.and(support.startTime.loe(LocalDateTime.now())) //시작 날짜 <= 현재 날짜
                .and(support.endTime.goe(LocalDateTime.now())); // 마감 날짜 >= 현재 날짜
        } else { // 마감된 후원글
            booleanBuilder.and(support.endTime.lt(LocalDateTime.now())); //마감 날짜 < 현재 날짜
        }
        booleanBuilder.and(support.status.eq(SupportStatus.ACCEPTED)); // 승인된 후원글
        return booleanBuilder;
    }
}
