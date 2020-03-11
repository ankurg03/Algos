package tree.bst;

public class KthSmallest {

    static int smallestIndex = 1;
    static int ans = -1;
    static boolean foundAns = false;
    public static void main(String[] args) {
        String [] input = {"3","1","4",null,"2"};
        TreeNode root = new TreeNode(3);
        TreeNode left = new TreeNode(1);
        TreeNode right = new TreeNode(4);
        TreeNode left_right = new TreeNode(2);
        root.left = left;
        root.right = right;
        root.left.right = left_right;


        /**
         *      3
         *    /    \
         *   1      4
         *     \
         *      2
         */

        System.out.println(kthSmallest(root, 1));

//        System.out.println(kthSmallest(root, 1));
//        System.out.println(kthSmallest(root, 2));
//        System.out.println(kthSmallest(root, 3));

    }


    public static int kthSmallest(TreeNode root, int k) {
        if (root == null || foundAns ) return ans;

        if(root.left != null) kthSmallest(root.left, k );

        System.out.println("-- " +smallestIndex + " va " + root.val);

        if (smallestIndex == k) {
            foundAns = true;
            ans = smallestIndex;
            return ans;
        }
        smallestIndex ++;

        if(root.right != null) kthSmallest(root.right, k);

        return ans;
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

}
