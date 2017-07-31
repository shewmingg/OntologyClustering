package gzm.ontology.birch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gzm.ontology.clustering.Cluster;
import gzm.ontology.clustering.Clustering;
 
public class Birch extends Clustering{
    LeafNode leafNodeHead;
    int point_num=0;        //point instance计数
    ArrayList<Cluster> __clusters = new ArrayList<Cluster>(); 
    
    public Birch(){
    	
    }
    
	public void buildClusters(ArrayList<List<Double>> sim) {
		TreeNode root  = buildBTree(sim);
		point_num = 0;
		StoreCluster(root);
	}
	public void StoreCluster(TreeNode root){
		if(!root.getClass().getName().equals("gzm.ontology.birch.LeafNode")){
            NonLeafNode nonleaf=(NonLeafNode)root;
            for(TreeNode child:nonleaf.getChildren()){
                StoreCluster(child);
            }
        }
        else{
          //  System.out.println("\n一个叶子节点:");
            LeafNode leaf=(LeafNode)root;
            for(MinCluster cluster:leaf.getChildren()){
         //       System.out.println("\n一个最小簇:");
                Cluster c = new Cluster(point_num);
                point_num++;
                for(String mark:cluster.getInst_marks()){
         //           System.out.print(mark+"\t");
                    c.AddIndex(Integer.valueOf(mark));
                }
                __clusters.add(c);
            }
        }
	}
	public ArrayList<Cluster> getClusters(){
		return __clusters;
	}
    public double[] ListToArray(List<Double> source){
    	double[] target = new double[source.size()];
    	 for (int i = 0; i < target.length; i++) {
    	    target[i] = source.get(i);                // java 1.5+ style (outboxing)
    	 }
    	return target;
    }
    //逐条扫描数据库，建立B-树
    public TreeNode buildBTree(ArrayList<List<Double>> sim){
    	
    	leafNodeHead = new LeafNode(sim.size());
        //先建立一个叶子节点
        LeafNode leaf=new LeafNode(sim.size());
        TreeNode root=leaf;
 
        //把叶子节点加入存储叶子节点的双向链表
        leafNodeHead.setNext(leaf);
        leaf.setPre(leafNodeHead);
        for(int i=0;i<sim.size();i++){	
        	CF cf=new CF(ListToArray(sim.get(i)));
            MinCluster subCluster=new MinCluster(sim.size());
            subCluster.setCf(cf);
            subCluster.getInst_marks().add(Integer.toString(i));
          //把新到的point instance插入树中
            root.absorbSubCluster(subCluster);
            //要始终保证root是树的根节点
            while(root.getParent()!=null){
                root=root.getParent();
            }
        }
        return root;
    }
     
    //打印B-树的所有叶子节点
    public void printLeaf(LeafNode header){
        //point_num清0
        point_num=0;
        while(header.getNext()!=null){
            System.out.println("\n一个叶子节点:");
            header=header.getNext();
            for(MinCluster cluster:header.getChildren()){
                System.out.println("\n一个最小簇:");
                for(String mark:cluster.getInst_marks()){
                    point_num++;
                    System.out.print(mark+"\t");
                }
            }
        }
    }
     
    //打印指定根节点的子树
    public void printTree(TreeNode root){
        if(!root.getClass().getName().equals("birch.LeafNode")){
            NonLeafNode nonleaf=(NonLeafNode)root;
            for(TreeNode child:nonleaf.getChildren()){
                printTree(child);
            }
        }
        else{
            System.out.println("\n一个叶子节点:");
            LeafNode leaf=(LeafNode)root;
            for(MinCluster cluster:leaf.getChildren()){
                System.out.println("\n一个最小簇:");
                for(String mark:cluster.getInst_marks()){
                    System.out.print(mark+"\t");
                    point_num++;
                }
            }
        }
    }
     
//    public static void main(String[] args) {
//        Birch birch=new Birch();
//        TreeNode root=birch.buildBTree("/home/orisun/test/iris.shuffled");
//        birch.point_num=0;
//        birch.printTree(root);
//        System.out.println();
//        //birch.printLeaf(birch.leafNodeHead);
//        //确认被分类的point instance和扫描数据库时录入的point instance的个数是一致的
//        System.out.println(birch.point_num);
//    }
}