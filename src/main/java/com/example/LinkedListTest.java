//package com.example;
//
//
//import java.util.Stack;
//
///**
// * @author LuoXianchao
// * @since 2024/11/04 20:05
// */
//class ListNode {
//  int val;
//  ListNode next = null;
//  public ListNode(int val) {
//    this.val = val;
//  }
//}
//public class LinkedListTest {
//
//    public static void main(String[] args) {
//        ListNode head = new ListNode(0);
//        ListNode node1 = new ListNode(1);
//        ListNode node2 = new ListNode(2);
//        ListNode node3 = new ListNode(3);
//        ListNode node4 = new ListNode(4);
//        ListNode node5 = new ListNode(5);
//        head.next = node1;
//        node1.next = node2;
//        node2.next = node3;
//        node3.next = node4;
//        node4.next = node5;
//        node5.next = null;
//
//        print(head);
//
//        // 反转
//        Stack<ListNode> stack = new Stack<>();
//        ListNode current = head.next;
//        while (current != null) {
//            stack.push(current);
//            current = current.next;
//        }
//
//        ListNode popCurrent = head;
//        while (!stack.isEmpty()) {
//            ListNode pop = stack.pop();
//            pop.next = null;
//            popCurrent.next = pop;
//            popCurrent = pop;
//        }
//
//        System.out.println();
//        print(head);
//
//    }
//
//    private static void print(ListNode head) {
//        ListNode current = head.next;
//        while (current != null) {
//            System.out.println(current.val);
//            current = current.next;
//        }
//    }
//}
