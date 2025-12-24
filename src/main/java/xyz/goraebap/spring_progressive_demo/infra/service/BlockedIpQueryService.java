package xyz.goraebap.spring_progressive_demo.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.spring_progressive_demo.infra.mapper.BlockedIpViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.BlockedIpViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination;

@Service
@RequiredArgsConstructor
public class BlockedIpQueryService {

    private final BlockedIpViewMapper blockedIpViewMapper;

    public Pagination<BlockedIpViewModel> getBlockedIpsWithPagination(
            String search,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;
        var items = blockedIpViewMapper.findAll(search, size, offset);
        int totalCount = blockedIpViewMapper.countAll(search);
        return Pagination.of(items, page, totalCount, "desc", size);
    }

    public BlockedIpViewModel getById(Long id) {
        return blockedIpViewMapper.findById(id);
    }
}
