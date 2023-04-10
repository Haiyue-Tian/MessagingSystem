package com.haiyue.messaging.response;

import com.haiyue.messaging.enums.Status;

import java.util.List;

import static com.haiyue.messaging.utils.Constant.PAGE_SIZE;

public class PaginatedResponse<T> extends CommonResponse{

    private boolean hasNext;
    private int page;
    private List<T> data;

    public PaginatedResponse(List<T> data, int page) {
        super(Status.OK);
        this.page = page;
        this.hasNext = data.size() > PAGE_SIZE;
        this.data = data.subList(0,  Math.min(data.size(), PAGE_SIZE));
    }

    public boolean hasNext() {
        return hasNext;
    }

    public int getPage() {
        return page;
    }

    public List<T> getData() {
        return data;
    }
}