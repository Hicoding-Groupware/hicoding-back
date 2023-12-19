package com.hook.hicodingapi.common.paging;

import com.hook.hicodingapi.board.dto.response.PostReadResponse;
import com.hook.hicodingapi.comment.dto.response.CommentReadResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CustomPagination {

    public static int DEFAULT_PAGE_POST_COUNT = 10;
    public static int DEFAULT_UI_BUTTON_COUNT = 5;

    private static int calculateMaxPage(final int objectSize, final Integer pagePostCnt) {
        int postPageCnt = pagePostCnt == null ? DEFAULT_PAGE_POST_COUNT : pagePostCnt;
        int maxPage = (int) Math.ceil((double) objectSize / postPageCnt);
        return maxPage;
    }

    private static PagingButtonInfo getPagingButtonInfo(final int currPage, final int maxPage, final Integer defaultBtnCnt) {
        final int selectedPageUIButtonCount = defaultBtnCnt == null ? DEFAULT_UI_BUTTON_COUNT : defaultBtnCnt;

        int startPage = (currPage - 1) / selectedPageUIButtonCount * selectedPageUIButtonCount + 1;
        int endPage = startPage + selectedPageUIButtonCount - 1;

        if (maxPage < endPage)
            endPage = maxPage;

        if (maxPage == 0 && endPage == 0)
            endPage = startPage;

        return new PagingButtonInfo(currPage, startPage, endPage, maxPage);
    }


    // 오브젝트의 크기를 페이지에 맞게 걸러내 반환한다.
    private static Object getFilteredObjects(final String objType, final Object object, final int objSize, final Integer pagePostCnt, final PagingButtonInfo pagingButtonInfo) {
        List<Object> filteredObjList = new ArrayList<>();

        Integer selectedPagePostCnt = pagePostCnt;
        if (pagePostCnt == null)
            selectedPagePostCnt = DEFAULT_PAGE_POST_COUNT;

        final int currPage = pagingButtonInfo.getCurrentPage();
        final int startIdx = (currPage - 1) * selectedPagePostCnt;
        final int endIdx = currPage * selectedPagePostCnt < objSize ? currPage * selectedPagePostCnt : objSize;

        // 계층구조를 dfs를 사용하여 리스트로 변환한다.
        if (object instanceof ArrayList) {
            switch (objType) {
                case "Post":
                    List<PostReadResponse> cvrPostReadList = (List<PostReadResponse>) object;
                    int cnt = -1;
                    for (int i = 0; i < cvrPostReadList.size(); i++) {
                        //List<PostReadResponse> result = new ArrayList<>(); // 결과를 저장할 리스트
                        Stack<PostReadResponse> stack = new Stack<>();

                        stack.push(cvrPostReadList.get(i));

                        ++cnt;

                        while (!stack.isEmpty()) {
                            PostReadResponse current = stack.pop();
                            //result.add(current); // 객체를 리스트에 추가

                            if (startIdx <= cnt && cnt < endIdx)
                                filteredObjList.add(current); // 객체를 리스트에 추가\

                            // 자식 댓글들을 역순으로 스택에 추가
                            List<PostReadResponse> childrenList = current.getChildrenList();
                            if (childrenList != null) {
                                for (PostReadResponse child : childrenList) {
                                    stack.push(child);

                                    ++cnt;
                                }
                            }
                        }
                    }

                    break;
                case "Comment":
                    List<CommentReadResponse> cvrCmtList = (List<CommentReadResponse>) object;
                    int renderCnt = -1;
                    for (int i = 0; i < cvrCmtList.size(); i++) {
                        //List<PostReadResponse> result = new ArrayList<>(); // 결과를 저장할 리스트
                        Stack<CommentReadResponse> stack = new Stack<>();

                        stack.push(cvrCmtList.get(i));

                        ++renderCnt;

                        while (!stack.isEmpty()) {
                            CommentReadResponse current = stack.pop();
                            //result.add(current); // 객체를 리스트에 추가

                            if (startIdx <= renderCnt && renderCnt < endIdx)
                                filteredObjList.add(current); // 객체를 리스트에 추가\

                            // 자식 댓글들을 역순으로 스택에 추가
                            List<CommentReadResponse> childrenList = current.getChildrenList();
                            if (childrenList != null) {
                                for (CommentReadResponse child : childrenList) {
                                    stack.push(child);

                                    ++renderCnt;
                                }
                            }
                        }
                    }

                    break;
            }
        }

        return filteredObjList;
    }

    public static PagingResponse getPagingResponse(final String objType, final Object object, final int objectCnt, final int currPage, final Integer pagePostCnt, final Integer defaultBtnCnt) {
        final PagingButtonInfo pagingButtonInfo = getPagingButtonInfo(currPage, calculateMaxPage(objectCnt, pagePostCnt), defaultBtnCnt);
        final Object filteredObj = getFilteredObjects(objType, object, objectCnt, pagePostCnt, pagingButtonInfo);
        return PagingResponse.of(filteredObj, pagingButtonInfo);
    }

    public static Pageable getPageable(final Integer page, final Integer pageItemCount, final String objectCode, boolean isAscending) {
        return PageRequest.of(
                page - 1,
                pageItemCount,
                isAscending ? Sort.by(objectCode).ascending() : Sort.by(objectCode).descending()
        );
    }
}
