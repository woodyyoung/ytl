package com.lty.rt.basicData.treeUtil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class TreeNodeUtils {

	public static List<TreeViewEntity> generateTreeViewEntity(List<TreeNode> nodeList) {
		if (CollectionUtils.isEmpty(nodeList)) {
			return null;
		}
		return getChildenNode("-1", 0, nodeList);
	}

	private static List<TreeViewEntity> getChildenNode(String id, int level, List<TreeNode> nodeList) {
		List<TreeViewEntity> nodes = new ArrayList<TreeViewEntity>();
		for (TreeNode node : nodeList) {
			if (id.equals(node.getPid())) {
				TreeViewEntity treeView = new TreeViewEntity();
				treeView.setId(node.getId());
				treeView.setLevel(level + 1);
				treeView.setText(node.getNodeName());
				treeView.setNodes(getChildenNode(node.getId(), level + 1, nodeList));
				List<String> args = new ArrayList<String>();
				if (CollectionUtils.isEmpty(treeView.getNodes())) {
					args.add("0");
				} else {
					args.add("" + treeView.getNodes().size());
				}
				treeView.setTags(args);
				treeView.setArg(node.getArg());
				treeView.setArg1(node.getArg1());
				nodes.add(treeView);
			}
		}

		if (CollectionUtils.isEmpty(nodes)) {
			return null;
		} else {
			return nodes;
		}
	}

	public static List<TreeViewEntity> generateLevelTreeViewEntity(List<TreeNode> nodeList) {
		if (CollectionUtils.isEmpty(nodeList)) {
			return null;
		}
		return getLevelChildenNode("-1", 0, nodeList);
	}

	private static List<TreeViewEntity> getLevelChildenNode(String id, int level, List<TreeNode> nodeList) {
		List<TreeViewEntity> nodes = new ArrayList<TreeViewEntity>();
		for (TreeNode node : nodeList) {
			if (id.equals(node.getPid())) {
				TreeViewEntity treeView = new TreeViewEntity();
				treeView.setId(node.getId());
				treeView.setLevel(level + 1);
				treeView.setText(node.getNodeName());
				treeView.setNodes(getLevelChildenNode(node.getId(), level + 1, nodeList));
				List<String> args = new ArrayList<String>();
				if (CollectionUtils.isEmpty(treeView.getNodes())) {
					if (level == 1) {
						treeView.setPid(id);
						if ("1".equals(node.getArg1())) {
							args.add("已启用");
						} else {
							args.add("已禁用");
						}
					} else {
						treeView.setPid(node.getId());
						args.add("0");
					}
				} else {
					treeView.setPid(node.getId());
					args.add("" + treeView.getNodes().size());
				}
				treeView.setTags(args);
				treeView.setArg(node.getArg());
				nodes.add(treeView);
			}
		}

		if (CollectionUtils.isEmpty(nodes)) {
			return null;
		} else {
			return nodes;
		}
	}

}
