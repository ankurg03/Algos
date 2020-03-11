package tree.traversal.inorder;

import java.util.ArrayList;
import java.util.List;

/**
 * //Given a binary tree, return the inorder traversal of its nodes' values.
 */
public class inorderTravelsal {


    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList();
        if (root == null) return list;
        return inorderTraversal(root, list);
    }

    public List<Integer> inorderTraversal(TreeNode root, List<Integer> ans) {
        if (root.left != null) inorderTraversal(root.left, ans);
        ans.add(root.val);
        if (root.right != null) inorderTraversal(root.right, ans);
        return ans;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
