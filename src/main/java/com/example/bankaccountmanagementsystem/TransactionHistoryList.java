package com.example.bankaccountmanagementsystem;

import java.util.ArrayList;
import java.util.List;

class TransactionHistoryList implements Iterable<String> {

    private static class Node {
        String transaction;
        Node next;

        Node(String transaction) {
            this.transaction = transaction;
        }
    }

    private Node head;

    public void add(String transaction) {
        Node newNode = new Node(transaction);
        newNode.next = head;
        head = newNode;
    }

    public List<String> getLastN(int n) {
        List<String> result = new ArrayList<>();
        Node current = head;
        int count = 0;
        while (current != null && count < n) {
            result.add(current.transaction);
            count++;
            current = current.next;
        }
        return result;
    }


    @Override
    public java.util.Iterator<String> iterator() {
        return new java.util.Iterator<String>() {
            private Node current = head;

            public boolean hasNext() {
                return current != null;
            }

            public String next() {
                String transaction = current.transaction;
                current = current.next;
                return transaction;
            }
        };
    }
}