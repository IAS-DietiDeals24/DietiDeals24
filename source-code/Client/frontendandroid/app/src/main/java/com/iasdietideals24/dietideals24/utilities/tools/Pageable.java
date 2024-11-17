package com.iasdietideals24.dietideals24.utilities.tools;

public class Pageable {

    private int pageNumber = 0;
    private int pageSize = 20;
    private Sort sort = new Sort();
    private int offset = 0;
    private boolean unpaged = false;
    private boolean paged = true;

    public Pageable() {}

    public Pageable(int pageNumber, int pageSize, Sort sort, int offset, boolean unpaged, boolean paged) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
        this.offset = offset;
        this.unpaged = unpaged;
        this.paged = paged;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Sort getSort() {
        return sort;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isUnpaged() {
        return unpaged;
    }

    public boolean isPaged() {
        return paged;
    }
}
