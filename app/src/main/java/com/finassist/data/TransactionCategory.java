package com.finassist.data;


import java.io.Serializable;

public class TransactionCategory implements Serializable {
    private String id = null;
    private String name;
    private TransactionCategory parent = null;

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TransactionCategory that = (TransactionCategory) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return parent != null ? parent.equals(that.parent) : that.parent == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (parent != null ? parent.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		if (parent == null) {
			return name;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(parent.getName() + " - " + name);

		return sb.toString();
	}

	/*
	@Override
    public String toString() {
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
    */
}
