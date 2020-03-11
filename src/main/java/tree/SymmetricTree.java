package tree;

public class SymmetricTree {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(2);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(3);

                /**

                 1
            2           2
       3        4 4         3

                 **/
        System.out.println(isSymmetric(root));

    }

    public static boolean isSymmetric(TreeNode root) {
        if(root == null) return true;
        return compare(root.left, root.right);
    }

    private static boolean compare(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) return true;
        if ((root1 != null && root2 == null) || (root1 == null && root2 != null)) return  false;
        if(root1.val != root2.val) return false;
        return compare(root1.left, root2.right) && compare(root1.right, root2.left);
    }

    public static class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
}
