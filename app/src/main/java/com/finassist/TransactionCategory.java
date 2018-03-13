package com.finassist;


public class TransactionCategory {
    String name;
    TransactionCategory parent = null;

    public TransactionCategory(String name) {
        this.name = name;
    }

    public TransactionCategory(String name, TransactionCategory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionCategory getParent() {
        return parent;
    }

    public void setParent(TransactionCategory parent) {
        this.parent = parent;
    }
}
