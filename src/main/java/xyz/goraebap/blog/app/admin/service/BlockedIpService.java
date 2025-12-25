package xyz.goraebap.blog.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.blog.app.admin.domain.BlockedIpEntity;
import xyz.goraebap.blog.app.admin.domain.BlockedIpRepository;
import xyz.goraebap.blog.app.admin.dto.BlockedIpFormRequest;
import xyz.goraebap.blog.shared.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BlockedIpService {

    private final BlockedIpRepository blockedIpRepository;

    /**
     * 수동 IP 차단
     */
    @Transactional
    public BlockedIpEntity block(BlockedIpFormRequest req) {
        // 이미 차단된 IP인지 확인
        var existing = blockedIpRepository.findByIpAddress(req.getIpAddress());
        if (existing.isPresent()) {
            var entity = existing.get();
            // 이미 활성 상태면 그대로 반환
            if (entity.isActive()) {
                return entity;
            }
            // 비활성 상태면 삭제 후 새로 생성
            blockedIpRepository.delete(entity);
        }

        var entity = BlockedIpEntity.createManual(req.getIpAddress(), req.getReason());
        return blockedIpRepository.save(entity);
    }

    /**
     * 자동 IP 차단 (WAF용)
     */
    @Transactional
    public void autoBlock(String ipAddress, String reason, int hours) {
        // 이미 활성 차단이 있으면 스킵
        var existing = blockedIpRepository.findActiveByIpAddress(ipAddress);
        if (existing.isPresent()) {
            return;
        }

        // 비활성 레코드가 있으면 삭제
        blockedIpRepository.findByIpAddress(ipAddress).ifPresent(blockedIpRepository::delete);

        var entity = BlockedIpEntity.createAuto(ipAddress, reason, hours);
        blockedIpRepository.save(entity);
    }

    /**
     * IP 차단 여부 확인 (WAF용)
     */
    @Transactional(readOnly = true)
    public boolean isBlocked(String ipAddress) {
        return blockedIpRepository.findActiveByIpAddress(ipAddress).isPresent();
    }

    /**
     * 차단 해제
     */
    @Transactional
    public void unblock(Long id) {
        var entity = blockedIpRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("차단 IP를 찾을 수 없습니다."));
        entity.deactivate();
        blockedIpRepository.save(entity);
    }

    /**
     * 영구 차단으로 변경
     */
    @Transactional
    public void makePermanent(Long id) {
        var entity = blockedIpRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("차단 IP를 찾을 수 없습니다."));
        entity.makePermanent();
        blockedIpRepository.save(entity);
    }

    /**
     * 차단 레코드 삭제
     */
    @Transactional
    public void delete(Long id) {
        var entity = blockedIpRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("차단 IP를 찾을 수 없습니다."));
        blockedIpRepository.delete(entity);
    }

    /**
     * 만료된 차단 일괄 비활성화
     */
    @Transactional
    public int expireOldBlocks() {
        return blockedIpRepository.expireOldBlocks();
    }
}
