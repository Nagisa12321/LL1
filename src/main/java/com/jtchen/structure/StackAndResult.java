package com.jtchen.structure;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/6 14:06
 */
public class StackAndResult {
    private final String stackLine;
    private final boolean result;

    public StackAndResult(String stackLine, boolean result) {
        this.stackLine = stackLine;
        this.result = result;
    }

    public String getStackLine() {
        return stackLine;
    }

    public boolean isSuccess() {
        return result;
    }
}
