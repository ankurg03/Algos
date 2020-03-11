package linkedlist.reverse;

public class ReverseLinkedList {


    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = null;

       ListNode head = null;
       ListNode newHead = reverseList(head);
        while (newHead != null) {
            System.out.print(newHead.val + ",");
            newHead = newHead.next;
        }



    }

    public static ListNode reverseList(ListNode head) {
        if (head == null) return head;
        ListNode newHead = head;
        while (newHead.next != null) {
            newHead = newHead.next;
        }
        reverseList(null, head);
        return newHead;
    }


    public static void reverseList(ListNode prev, ListNode current) {
        if (current.next != null) reverseList(current, current.next);
         current.next = prev;
    }



    public static class ListNode {
          int val;
          ListNode next;
          ListNode(int x) { val = x; }
    }

}
