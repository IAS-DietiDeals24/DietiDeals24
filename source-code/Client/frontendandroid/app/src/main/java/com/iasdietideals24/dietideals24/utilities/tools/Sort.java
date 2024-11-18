package com.iasdietideals24.dietideals24.utilities.tools;

public class Sort {

    private boolean unsorted = true;
    private boolean sorted = false;
    private boolean empty = true;

    public Sort() {
    }

    public Sort(boolean unsorted, boolean sorted, boolean empty) {
        this.unsorted = unsorted;
        this.sorted = sorted;
        this.empty = empty;
    }

    public boolean isUnsorted() {
        return unsorted;
    }

    public boolean isSorted() {
        return sorted;
    }

    public boolean isEmpty() {
        return empty;
    }
}
