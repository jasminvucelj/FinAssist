package com.finassist.data;


import java.io.Serializable;

public class TransactionCategory implements Serializable {
    String id = null;
    String name;
    TransactionCategory parent = null;

    public TransactionCategory() {}

    public TransactionCategory(String name) {
        this.name = name;
    }

    public TransactionCategory(String name, TransactionCategory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        // TODO test!
        if (parent == null) {
            return name;
        }

        StringBuilder sb = new StringBuilder(name);
        sb.append(" (");

        TransactionCategory currentParent = parent;
        while(true) {
            sb.append(currentParent.getName());
            if(currentParent.getParent() == null) break;
            sb.append(" - ");
            currentParent = currentParent.getParent();
        }

        sb.append(")");

        return sb.toString();
    }
}
