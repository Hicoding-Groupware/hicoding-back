package com.hook.hicodingapi.common.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPagination {

    public static int DEFAULT_PAGE_POST_COUNT = 10;
    public static int DEFAULT_UI_BUTTON_COUNT = 5;

    private static int calculateMaxPage(final int objectSize, final Integer pagePostCnt) {
        int maxPage = objectSize / (pagePostCnt == null ? DEFAULT_PAGE_POST_COUNT : pagePostCnt);
        return maxPage;
    }

    private static PagingButtonInfo getPagingButtonInfo(final int currPage, final int maxPage, final Integer defaultBtnCnt) {

        final int selectedPageUIButtonCount = defaultBtnCnt == null ? DEFAULT_UI_BUTTON_COUNT : defaultBtnCnt;

        int startPage = (currPage - 1) / selectedPageUIButtonCount * selectedPageUIButtonCount + 1;
        int endPage = startPage + selectedPageUIButtonCount - 1;

        if(maxPage < endPage)
            endPage = maxPage;

        if(maxPage == 0 && endPage == 0)
            endPage = startPage;

        return new PagingButtonInfo(currPage, startPage, endPage, maxPage);
    }

    public static PagingResponse getPagingResponse(final Object object, final int objectCnt, final int currPage, final Integer pagePostCnt, final Integer defaultBtnCnt) {
        final PagingButtonInfo pagingButtonInfo = getPagingButtonInfo(currPage, calculateMaxPage(objectCnt, pagePostCnt), defaultBtnCnt);
        return PagingResponse.of(object, pagingButtonInfo);
    }

    public static Pageable getPageable(final Integer page, final Integer pageItemCount, final String objectCode, boolean isAscending) {
        return PageRequest.of(
                page - 1,
                pageItemCount,
                isAscending ? Sort.by(objectCode).ascending() : Sort.by(objectCode).descending()
        );
    }
}
