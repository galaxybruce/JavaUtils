package com.galaxybruce.interview;

/**
 * 链表
 */
public class LinkTest {

    public static void main(String[] args) {
        // 1. 判断一个链表是否为回文结构
        // isPail(ListNode)
    }

    static class ListNode {
        int val;
        ListNode next;
    }

    /**
     * 反转指针：利用指针只能从前往后走的原理
     * @param head 指向中点的指针
     * @return
     */
    private static ListNode reverse(ListNode head) {
        ListNode pre = null;
        while(head != null) {
            ListNode next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    /**
     * 判断一个链表是否为回文结构
     * @param head
     * @return
     */
    private static boolean isPail(ListNode head) {
        if(head == null) {
            return false;
        }

        // 指向中点的链表
        ListNode slow = head;
        ListNode fast = head;
        // 双指针找中点
        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        slow = reverse(slow);
        fast = head;
        while(slow != null) {
            if(fast.val != slow.val) {
                return false;
            }
            fast = fast.next;
            slow = slow.next;
        }
        return true;
    }
}
