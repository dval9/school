/**
 *Assignment 3 CPSC 331 T01
 *@author Tom Crowfoot 10037477
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Class that implements SimpleSortedMap using a binary search tree.
 */
@SuppressWarnings("unchecked")
public class BSTMap<K extends Comparable<K>,V> implements SimpleSortedMap<K,V>{

    private Node<K,V> root;
    private int size;

    public BSTMap(){
		root=null;
		size=0;
    }

    /**
     * Tests whether the map is empty.
     * @return true if the dictionary is empty, false otherwise
     */
    public boolean isEmpty(){
		return (size==0);
    }

    /**
     * Returns the number of key-values pairs in the map.
     * @returns int The size of the map.
     */
    public int size(){
		return size;
    }

	/**
	 * Calculates the height of the binary tree.
	 */
	public int height(){
		Node<K,V> current=root;
		return height(current);
	}
	
	/**
	 * Helper function for height()
	 * @param Node Tree to calculate height of.
	 */
	public int height(Node<K,V> node){
		if(node==null)
			return -1;
		return Math.max(height(node.left),height(node.right))+1;
	}
	
    /**
     * Inserts a key-value pair into the map.
     * @param key The key to be inserted.
     * @param value Key's corresponding value.
     * @throws KeyFoundException If a matching key is already present in the map.
     */
    public void insert(K key, V value) throws KeyFoundException{
		Node<K,V> current=root;
		Node<K,V> parent=null;
		if(current==null){
			root=new Node<K,V>(key,value,null,null,null);
			size++;
		}
		else{
			while(current!=null){
				if((current.key).compareTo(key)==0)
					throw new KeyFoundException();
				else if((current.key).compareTo(key)<0){
					parent=current;
					current=current.left;
					if(current==null){
						Node<K,V> n=new Node<K,V>(key,value,null,null,parent);
						parent.left=n;
						size++;
						return;
					}
				}
				else{
					parent=current;
					current=current.right;
					if(current==null){
						Node<K,V> n=new Node<K,V>(key,value,null,null,parent);
						parent.right=n;
						size++;
						return;
					}
				}
			}
		}
    }
	    
    /**
     * Deletes the key-value pair with the specified key from the map
     * @param key The key to remove from the map.
     * @throws KeyNotFoundException If key is not found in the map.
     */
    public void delete(K key) throws KeyNotFoundException{
		Node<K,V> current=root;
		Node<K,V> parent=null;
		while(current!=null){
			if((current.key).compareTo(key)==0){
				if(current.left==null&&current.right==null){
					if(current==root)
						root=null;
					else if(parent.left==current)
						parent.left=null;
					else
						parent.right=null;
				}
				else if(current.left!=null&&current.right==null){
					if(current==root){
						root=current.left;
						current.left.parent=null;
					}
					else if(parent.left==current){
						parent.left=current.left;
						current.left.parent=current.parent;
					}
					else{
						parent.right=current.left;
						current.left.parent=current.parent;
					}
				}
				else if(current.left==null&&current.right!=null){
					if(current==root){
						root=current.right;
						current.right.parent=null;
					}
					else if(parent.left==current){
						parent.left=current.right;
						current.right.parent=current.parent;
					}
					else{
						parent.right=current.right;
						current.right.parent=current.parent;
					}
				}
				else{
					Node<K,V> min=current.right;
					while(min.left!=null){
						min=min.left;
					}
					if(current==root){
						if(current.right==min){
							min.parent=null;
							min.left=current.left;
							current.left.parent=min;
							root=min;
						}
						else{
							min.parent.left=min.right;
							if(min.right!=null)
								min.right.parent=min.parent;
							min.parent=null;
							min.left=current.left;
							min.right=current.right;
							current.left.parent=min;
							current.right.parent=min;
							root=min;
						}
					}
					else if(parent.left==current){
						if(current.right==min){//ADD HERE
							min.parent=current.parent;
							min.left=current.left;
							current.left.parent=min;
							parent.left=min;
						}
						else{
							min.parent.left=min.right;
							if(min.right!=null)
								min.right.parent=min.parent;
							min.parent=current.parent;
							min.left=current.left;
							min.right=current.right;
							current.left.parent=min;
							current.right.parent=min;
							parent.left=min;
						}
					}
					else{
						if(current.right==min){//ADD HERE
							min.parent=current.parent;
							min.left=current.left;
							current.left.parent=min;
							parent.right=min;
						}
						else{
							min.parent.left=min.right;
							if(min.right!=null)
								min.right.parent=min.parent;
							min.parent=current.parent;
							min.left=current.left;
							min.right=current.right;
							current.left.parent=min;
							current.right.parent=min;
							parent.right=min;
						}
					}
				}
				size--;
				current=null;
				return;
			}
			else if((current.key).compareTo(key)<0){
				parent=current;
				current=current.left;
			}
			else{
				parent=current;
				current=current.right;
			}
		}
		throw new KeyNotFoundException();
    }

    /**
     * Returns the value corresponding to key.
     * @param key The key to search for in the map.
     * @returns V The value corresponding to key.
     * @throws KeyNotFoundException If key is not found in the map.
     */
    public V search(K key) throws KeyNotFoundException{
		Node<K,V> current=root;
		while (current!=null){
			if((current.key).compareTo(key)==0)
				return current.value;
			else if((current.key).compareTo(key)<0)
				current=current.left;
			else
				current=current.right;
		}
		throw new KeyNotFoundException();
    }

    /**
     * Modifies the value corresponding to key in the map.
     * @param key The key whose value to modify.
     * @param value The new value of key.
     * @throws KeyNotFoundException If key was not found in the map and therefore no value was modified.
     */
    public void modify(K key, V value) throws KeyNotFoundException{
		Node<K,V> current=root;
		while (current!=null){
			if((current.key).compareTo(key)==0){
				current.value=value;
				return;
			}
			else if((current.key).compareTo(key)<0)
				current=current.left;
			else
				current=current.right;
		}
		throw new KeyNotFoundException();
    }

    /**
     * Returns a new iterator.
     * @returns Iterator<K> A new iterator.
     */
    public Iterator<K> iterator(){
		return new BSTMapIterator<K,V>();
    }

    /**
     * Class to iterate through the BST inorder.
     */
    private class BSTMapIterator<K extends Comparable<K>,V> implements Iterator<K>{
		private Node<K,V> top,cursor;
		private Stack stack;

		BSTMapIterator(){
			top=(Node<K,V>)root;
			cursor=(Node<K,V>)root;
			stack=new Stack();
		}

		/**
		 * Returns if the map has a next element.
		 * @return true if current is not null, false otherwise.
		 */
		public boolean hasNext(){
			return (!stack.empty()||cursor!=null);
		}

		/**
		 * Returns the next key in the map, values returned inorder(ascending).
		 * @return K The key of the next node.
		 */
		public K next(){
			Node<K,V> temp;
			while(cursor!=null){
				stack.push(cursor);
				cursor=cursor.left;
			}
			cursor=(Node<K,V>)stack.pop();
			temp=cursor;
			cursor=cursor.right;
			return temp.key;
		}

		/**
		 * Unsupported method, required for Iterator interface however.
		 */
		public void remove(){
			throw new UnsupportedOperationException();
		}
    }

    /**
     * Structure to define a node.
     * Contains values for key, value, left child, right child, and parent.
     */
    private class Node<K extends Comparable<K>,V>{
		private K key;
		private V value;
		private Node<K,V> left;
		private Node<K,V> right;
		private Node<K,V> parent;

		Node(K k,V v,Node<K,V> l,Node<K,V> r,Node<K,V> p){
			key=k;
			value=v;
			left=l;
			right=r;
			parent=p;
		}
    }
}